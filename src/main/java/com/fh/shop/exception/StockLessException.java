package com.fh.shop.exception;

public class StockLessException extends RuntimeException {

    public StockLessException(String message){
          super(message);

    }
}
