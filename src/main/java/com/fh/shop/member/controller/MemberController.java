package com.fh.shop.member.controller;

import com.fh.shop.annotation.Check;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.member.biz.MemberService;
import com.fh.shop.member.po.Member;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/members")
@Api(tags = "会员接口")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping
    @ApiOperation("会员注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberName",value="会员名称",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="password",value="密码",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="realName",value="真实姓名",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="birthday",value="生日",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="mail",value="邮箱",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="phone",value="手机号",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="shengId",value="省ID",type="long",required =false,paramType = "query"),
            @ApiImplicitParam(name="shiId",value="市ID",type="long",required =false,paramType = "query"),
            @ApiImplicitParam(name="xianId",value="县ID",type="long",required =false,paramType = "query"),
            @ApiImplicitParam(name="areaName",value="地区名称",type="String",required =false,paramType = "query"),
    })
    public ServerResponse add(Member member) throws Exception {
        return memberService.add(member);
    }

    @GetMapping("validaterMemName")

    @ApiOperation("会员名称查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberName",value="会员名称",type="string",required =true,paramType = "query"),

    })
    public ServerResponse validaterMemName(String memberName) {
        return memberService.validaterMemName(memberName);

    }

    @GetMapping("validaterPhone")

    @ApiOperation("手机号查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value="会员名称",type="string",required =true,paramType = "query"),

    })
    public ServerResponse validaterPhone(String phone) {
        return memberService.validaterPhone(phone);

    }

    @GetMapping("validaterEmail")

    @ApiOperation("邮箱查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mail",value="会员名称",type="string",required =true,paramType = "query"),

    })
    public ServerResponse validaterEmail(String mail) {
        return memberService.validaterEmail(mail);
    }
    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",value="会员名称",type="string",required =true,paramType = "query"),
            @ApiImplicitParam(name="password",value="密码",type="string",required =true,paramType = "query"),

    })
    public ServerResponse login(String userName ,String password){
        return memberService.login(userName,password);

    }
    @PostMapping("/loginMember")
    @Check
    @ApiOperation("登录会员信息")
    public ServerResponse loginMember(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
           return  ServerResponse.success(memberVo);
    }
    @GetMapping("/logoutMember")
    @Check
    @ApiOperation("退出")
    public ServerResponse logoutMember(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();
        String uuid = memberVo.getUuid();
        RedisUtil.del(Keyutil.buildMemberKey(uuid,id));
        return  ServerResponse.success(memberVo);
    }

}