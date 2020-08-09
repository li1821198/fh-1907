package com.fh.shop.utils;

public class Keyutil {
              public static final int Member_EXPIRE=10*60;


    public static String buildMemberKey(String uuid ,Long id){
         return "member:"+uuid+":"+id;

     }

    public static String buildCartKey(Long memberId) {
        return "cart:"+memberId;
     }

    public static String buildStockLess(Long memberId) {
        return "order:stock:less:"+memberId;
    }

    public static String buildOrderKey(Long memberId) {
        return "order:"+memberId;
    }

    public static String buildPayLogKey(Long memberId) {
        return "payLog:"+memberId;
    }

    public static String buildOrderErrorKey(Long memberId) {
        return  "order:error:"+memberId;
    }
}
