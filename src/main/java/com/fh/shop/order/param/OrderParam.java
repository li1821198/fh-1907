package com.fh.shop.order.param;

import lombok.Data;

@Data
public class OrderParam {
    private Long memberId;
    private Long recipientId;
    private int PayType;
}
