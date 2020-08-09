package com.fh.shop.exception;

import com.fh.shop.common.ResponseEnum;

public class TokenException extends  RuntimeException {
    private ResponseEnum responseEnum;

    public TokenException(ResponseEnum responseEnum){
        this.responseEnum=responseEnum;
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }
}
