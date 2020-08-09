package com.fh.shop.common;

public enum ResponseEnum {
    ORDER_STOCK_LESS(4000,"下订单时库存不足"),
    ORDER_QUEUE(4001,"订单这个在排队中,请稍等"),
    ORDER_Error(4002,"下订单失败"),

    PAY_IS_ERROR(5000,"支付失败，已超时"),

    TOKEN_IS_MISS(6000,"token信息丢失"),
    TOKEN_IS_ERROR(6001,"token错误"),
    TOKEN_REQUEST_REPET(6002,"请求重复"),


    CART_PRODUCT_IS_NULL(3000,"添加商品不存在"),
    CART_PRODUCT_IS_STATUS(3001,"添加商品已下架"),
    CART_NUMBER_ERROR(3002,"商品数量错误"),

    LOGIN_MEMBER_IS_NULL(2000,"用户名或密码为空"),
    LOGIN_MEMBERNAME_IS_EXIT(2001,"用户名不存在"),
    LOGIN_PASSWORD_IS_error(2002,"密码错误"),
    LOGIN_HANDLER_IS_MISS(2003,"头信息丢失"),
    LOGIN_HANDLER_CONTENT_IS_MISS(2004,"头信息不完整"),
    LOGIN_HANDLER_CONTENT_IS_MISS1(2005,"头信息被篡改"),
    LOGIN_TIME_OUT(2006,"登陆超时"),
    REG_MemberName_Exist(1001,"会员名称已存在"),
    REG_MemberMail_Exist(1002,"会员邮箱已存在"),
    REG_MemberPhone_Exist(1003,"会员手机号已存在"),
    REG_Member_Is_NULL(1000,"会员信息不能为空");

    private int code;
    private String msg;

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
