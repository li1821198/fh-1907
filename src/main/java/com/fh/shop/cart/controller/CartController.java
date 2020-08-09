package com.fh.shop.cart.controller;

import com.fh.shop.annotation.Check;
import com.fh.shop.cart.biz.CartService;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.member.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@Api(tags = "购物车接口")
public class CartController {
         @Autowired
          private CartService cartService;

    @PostMapping("/addCart")
    @Check
    @ApiOperation("添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token",value = "头信息",type = "string",required = true ,paramType = "header"),
            @ApiImplicitParam(name = "goodsId",value = "商品Id",type = "long",required = true ,paramType = "query"),
            @ApiImplicitParam(name = "num",value = "商品数量",type = "long",required = true ,paramType = "query")
    })
    public ServerResponse  addCart(HttpServletRequest request,Long goodsId ,int num){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return cartService.addCart(memberId,goodsId,num);
    }
    @GetMapping
    @Check
    @ApiOperation("查询购物车商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header")
    })
    public ServerResponse  findCartList(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();

        return cartService.findCartList(memberId);
    }
    @GetMapping("/findCartNum")
    @Check
    @ApiOperation("查询购物车商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header")
    })
    public ServerResponse findCartNum(HttpServletRequest request ){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return cartService.findCartNum(memberId);
    }
    @DeleteMapping("/deleteCate/{goodsId}")
    @Check
    @ApiOperation("查询购物车商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-token", value = "头信息", type = "string", required = true, paramType = "header"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", type = "string", required = true, paramType = "path")
    })
    public ServerResponse delGoods(HttpServletRequest request,@PathVariable("goodsId") Long goodsId){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return cartService.delGoods(memberId,goodsId);
    }



}
