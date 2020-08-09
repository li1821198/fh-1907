package com.fh.shop.interceptor;

import com.fh.shop.annotation.Idempotent;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.exception.TokenException;
import com.fh.shop.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (!method.isAnnotationPresent(Idempotent.class)) {
            return true;
        }
        String header = request.getHeader("x-auth");


        if (StringUtils.isEmpty(header)){
            throw  new TokenException(ResponseEnum.TOKEN_IS_MISS);
        }
        boolean exist = RedisUtil.exist(header);
        if (!exist){
           throw  new TokenException(ResponseEnum.TOKEN_IS_ERROR);
        }
        long flag = RedisUtil.del(header);
        if(flag==0){
            throw  new TokenException(ResponseEnum.TOKEN_REQUEST_REPET);
        }


        return true;
    }


}
