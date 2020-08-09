package com.fh.shop.pay.controller;

import com.fh.shop.annotation.Check;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.pay.biz.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/pay")
@Api(tags = "支付接口")
public class PayController {
        @Autowired
        private PayService payService;
     @Check
     @PostMapping("/createNative")
     @ApiOperation("统一下单")
     @ApiImplicitParams({
             @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header"),
     })
    public ServerResponse createNative(HttpServletRequest request){
         MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
         Long memberId = member.getId();
        return payService.createNative(memberId);
    }
    @Check
    @PostMapping("/queryStatus")
    @ApiOperation("查询支付状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header"),
    })
    public ServerResponse queryStatus(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return payService.queryStatus(memberId);
    }
}
