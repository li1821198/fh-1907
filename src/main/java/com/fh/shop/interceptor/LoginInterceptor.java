package com.fh.shop.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.fh.shop.annotation.Check;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.exception.GlobalException;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.MD5Util;
import com.fh.shop.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

           response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
           response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-token,content-type,x-auth");
           response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"GET POST DELETE PUT");

        String method1 = request.getMethod();
        if(method1.equalsIgnoreCase("options")){
            return false;

        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法上是否有@check这个注解
        if (!method.isAnnotationPresent(Check.class)) {
            return true;
        }
        //获取前台传递过来的头信息;
        String header = request.getHeader("x-token");
         //判断头信息是否为空
        if (StringUtils.isEmpty(header)) {
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_IS_MISS);
        }
        //将头信息经过.进行分割得到一个Base编码的用户信息,和Base64编码后的签名
        String[] split = header.split("\\.");
         //判断头信息是否完整
        if (split.length != 2) {
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_CONTENT_IS_MISS);
        }

        //经过分割后的Base64编码会员信息
        String memberBase64Json = split[0];
         //经过分割后得到的Base64编码的签名
        String signBase64 = split[1];
        //重新通过会员信息和密钥生成一个新签名
        String newsign = MD5Util.sign(memberBase64Json, MD5Util.SECRET);
        //将新签名经过Base64进行转码
        String newBase64sign = Base64.getEncoder().encodeToString(newsign.getBytes("utf-8"));
         //判断新签民和老签名是否一致如果一直则说明签名没被篡改
        if (!newBase64sign.equals(signBase64)) {
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_CONTENT_IS_MISS1);
        }
         //将base64转码后的会员信息解码
        String memberJson = new String(Base64.getDecoder().decode(memberBase64Json));
        //将json格式的字符串进行转换
         MemberVo memberVo = JSONObject.parseObject(memberJson, MemberVo.class);
         //重新获取UUID；
        String uuid = memberVo.getUuid();
         //重新获取id
        Long id = memberVo.getId();
      //通过exist获取redis;
         boolean exist=    RedisUtil.exist(Keyutil.buildMemberKey(uuid, id));
          //判断redis是否存在
         if (!exist){
             throw new GlobalException(ResponseEnum.LOGIN_TIME_OUT);
         }
        //通过expire给redis进行续命
         RedisUtil.expire(memberJson,Keyutil.Member_EXPIRE);
          //将会员对象存入到request中方便后期取数据
         request.setAttribute(SystemConstant.CURR_MEMBER,memberVo);

        return true;
    }

}
