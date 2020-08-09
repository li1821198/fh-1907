package com.fh.shop.pay.biz;

import com.fh.shop.common.ServerResponse;

public interface PayService {
    ServerResponse createNative(Long memberId);

    ServerResponse queryStatus(Long memberId);
}
