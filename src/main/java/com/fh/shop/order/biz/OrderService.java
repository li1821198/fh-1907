package com.fh.shop.order.biz;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.order.param.OrderParam;

public interface OrderService {
    ServerResponse generateOrderConfirm(Long memberId);

    ServerResponse generateOrder(OrderParam orderParam);

    void createOrder(OrderParam orderParam);

    ServerResponse getResult(Long memberId);

}
