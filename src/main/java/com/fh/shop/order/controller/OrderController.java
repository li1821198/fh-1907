package com.fh.shop.order.controller;

import com.fh.shop.annotation.Check;
import com.fh.shop.annotation.Idempotent;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.order.biz.OrderService;
import com.fh.shop.order.param.OrderParam;
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
@RequestMapping("/api/order")
@Api(tags = "订单接口")
public class OrderController  {
       @Autowired
       private OrderService orderService;
       @GetMapping("/generateOrderConfirm")
       @Check
       @ApiOperation("获取用户对应订单信息")

       @ApiImplicitParams({
               @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header"),

       })

    public ServerResponse  generateOrderConfirm(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return orderService.generateOrderConfirm(memberId);

    }
    @PostMapping("/generateOrder")
    @ApiOperation("创建订单")
    @Check
    @Idempotent
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header"),
            @ApiImplicitParam(name = "recipientId", value = "收件人Id", type = "long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付类型", type = "int", required = true, paramType = "query")
    })
    public ServerResponse  generateOrder(HttpServletRequest request, OrderParam orderParam){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        orderParam.setMemberId(memberId);

         return orderService.generateOrder(orderParam);
    }

    @GetMapping("/getResult")
    @Check
    @ApiOperation("获取订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header"),
    })
    public ServerResponse getResult(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
           return  orderService.getResult(memberId);

    }
}
