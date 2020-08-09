package com.fh.shop.exception;

import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebExceptionHandler {

    @ResponseBody
    @ExceptionHandler(GlobalException.class)
    public ServerResponse handleGlobalException(GlobalException e){
        ResponseEnum responseEnum = e.getResponseEnum();
            return ServerResponse.error(responseEnum);
    }
}
