package com.fh.shop.member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.mapper.MemberMapper;
import com.fh.shop.member.po.Member;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.mq.MQSender;
import com.fh.shop.mq.MailMessage;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.MD5Util;
import com.fh.shop.utils.MailUtil;
import com.fh.shop.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MailUtil mailUtil;
    @Autowired
    HttpServletRequest request;
    @Autowired
   private MQSender  mqSender;
    @Override

    public ServerResponse add(Member member) throws Exception {

        String memberName = member.getMemberName();
        String password = member.getPassword();
        String phone = member.getPhone();
        String mail = member.getMail();
        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(mail)
                || StringUtils.isEmpty(phone)) {
            return ServerResponse.error(ResponseEnum.REG_Member_Is_NULL);

        }
        QueryWrapper<Member> memberWapper = new QueryWrapper<>();
        memberWapper.eq("memberName", memberName);
        Member member1 = memberMapper.selectOne(memberWapper);


        if (member1 != null) {
            return ServerResponse.error(ResponseEnum.REG_MemberName_Exist);
        }
        QueryWrapper<Member> mailWapper = new QueryWrapper<>();
        mailWapper.eq("mail", mail);
        Member member2 = memberMapper.selectOne(mailWapper);


        if (member2 != null) {
            return ServerResponse.error(ResponseEnum.REG_MemberMail_Exist);
        }
        QueryWrapper<Member> phoneWapper = new QueryWrapper<>();
        phoneWapper.eq("phone", phone);
        Member member3 = memberMapper.selectOne(phoneWapper);


        if (member3 != null) {
            return ServerResponse.error(ResponseEnum.REG_MemberPhone_Exist);
        }


        memberMapper.add(member);

        return ServerResponse.success();
    }

    @Override
    public ServerResponse validaterMemName(String memberName) {
        if (StringUtils.isEmpty(memberName)) {
            return ServerResponse.error(ResponseEnum.REG_Member_Is_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper = new QueryWrapper();
        objectQueryWrapper.eq("memberName", memberName);
        Member member1 = memberMapper.selectOne(objectQueryWrapper);
        if (member1 != null) {
            return ServerResponse.error(ResponseEnum.REG_MemberName_Exist);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validaterEmail(String mail) {
        if (StringUtils.isEmpty(mail)) {
            return ServerResponse.error(ResponseEnum.REG_Member_Is_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper1 = new QueryWrapper();
        objectQueryWrapper1.eq("mail", mail);
        Member member2 = memberMapper.selectOne(objectQueryWrapper1);
        if (member2 != null) {
            return ServerResponse.error(ResponseEnum.REG_MemberMail_Exist);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validaterPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return ServerResponse.error(ResponseEnum.REG_Member_Is_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper2 = new QueryWrapper();
        objectQueryWrapper2.eq("phone", phone);
        Member member3 = memberMapper.selectOne(objectQueryWrapper2);
        if (member3 != null) {
            return ServerResponse.error(ResponseEnum.REG_MemberPhone_Exist);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(String userName, String password) {
        //进行用户名和密码的非空判断
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return ServerResponse.error(ResponseEnum.LOGIN_MEMBER_IS_NULL);
        }
        //将前台传过来的用户名和数据库的用户名进行比较
        QueryWrapper<Member> memberQueryMapper = new QueryWrapper<>();
        memberQueryMapper.eq("memberName", userName);
        Member members = memberMapper.selectOne(memberQueryMapper);
        //判断会员名称是否存在
        if (members == null) {
            return ServerResponse.error(ResponseEnum.LOGIN_MEMBERNAME_IS_EXIT);
        }
        //判断前台传过来的密码和数据库的密码是否一致
        if (!password.equals(members.getPassword())) {
            return ServerResponse.error(ResponseEnum.LOGIN_PASSWORD_IS_error);
        }
        //创建VO
        MemberVo memberVo = new MemberVo();
        //将id赋给vo
        memberVo.setId(members.getId());
        memberVo.setMemberName(members.getMemberName());
        memberVo.setUuid(UUID.randomUUID().toString());
        memberVo.setRealName(members.getRealName());
        //将vo对象通过第三方的jar包转换为Json格式字符串
        String memberJson = JSONObject.toJSONString(memberVo);
        try {
            //将转换的字符串经过Base64进行转码
            String s = Base64.getEncoder().encodeToString(memberJson.getBytes("utf-8"));
            //将签名经过MD5进行加密
            String sign = MD5Util.sign(s, MD5Util.SECRET);
            //将签名进行Base64进行转码
            String ss = Base64.getEncoder().encodeToString(sign.getBytes("utf-8"));
            //将member+uuid+id组合起来作为key 存到redis中 并设置其过期时间
            RedisUtil.setEX(Keyutil.buildMemberKey(memberVo.getUuid(), members.getId()), "", Keyutil.Member_EXPIRE);
            //返回相应的信息给前台
            String mail = members.getMail();

            members.getRealName();
            MailMessage mailMessage = new MailMessage();
                       mailMessage.setTo(mail);
                       mailMessage.setTotal("登陆成功");
                       mailMessage.setRealName(members.getRealName());
                       mailMessage.setContent("恭喜" + members.getRealName() + "登陆成功");





            mqSender.sendGoodsMessage(mailMessage);





            return ServerResponse.success(s + "." + ss);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



}
