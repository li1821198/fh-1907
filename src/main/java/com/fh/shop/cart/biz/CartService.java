package com.fh.shop.cart.biz;

import com.fh.shop.common.ServerResponse;

public interface CartService {

     ServerResponse addCart(Long memberId ,Long goodsId,int num);

     ServerResponse findCartList(Long memberId);

     ServerResponse findCartNum(Long memberId);


     ServerResponse delGoods(Long memberId, Long goodsId);
}
