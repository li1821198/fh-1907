package com.fh.shop.cart.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.cart.vo.Cart;
import com.fh.shop.cart.vo.CartInfo;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.product.mapper.ProductMapper;
import com.fh.shop.product.po.Product;
import com.fh.shop.utils.BigDecimalUtil;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductMapper productMapper;


    @Override
    public ServerResponse addCart(Long memberId, Long goodsId, int num) {

        Product product = productMapper.selectById(goodsId);
        //判断商品是否存在
        if (null == product) {

            return ServerResponse.error(ResponseEnum.CART_PRODUCT_IS_NULL);
        }
        //判断商品是否上架
        if (product.getStatus() == SystemConstant.PRODUCT_DOWN) {

            return ServerResponse.error(ResponseEnum.CART_PRODUCT_IS_STATUS);
        }
        //如果商品有对应的购物车
        String cartKey = Keyutil.buildCartKey(memberId);
        //从redis中取出key
        String cartJson = RedisUtil.get(cartKey);
        //判断购物车是否存在

        if (StringUtils.isNotEmpty(cartJson)) {
            //直接向购物车添加商品
            Cart cart = JSONObject.parseObject(cartJson, Cart.class);

            List<CartInfo> cartInfoList = cart.getCartInfo();

            CartInfo cartInfo = null;

            for (CartInfo item : cartInfoList) {

                if (item.getGoodsId().longValue() == goodsId.longValue()) {

                    cartInfo = item;

                    break;
                }
            }
            if (cartInfo != null) {
                //如果存在就更改商品小计和数量,更新购物车
                cartInfo.setNum(cartInfo.getNum() + num);
                //生成新的数量
                int num1 = cartInfo.getNum();
                if(num1 <=0){
                     cartInfoList.remove(cartInfo);
                }
                //计算总价格
                BigDecimal subPrice = BigDecimalUtil.nul(num1 + "", cartInfo.getGoodsPrice().toString());
                //更新总价格
                cartInfo.setSubPrice(subPrice);
                //更新购物车
                updateCart(memberId, cart);
                return ServerResponse.success(cart);
            } else {
                //如果商品不存在就添加商品,更新购物车
                if(num<=0){
                   return ServerResponse.error(ResponseEnum.CART_NUMBER_ERROR);
                }
               //构建商品
                CartInfo cartInfo1 = cartInfoItem(num, product);
                //添加商品
                cart.getCartInfo().add(cartInfo1);
              //更新购物车
                updateCart(memberId, cart);

                return ServerResponse.success(cart);
            }


        } else {
            if(num<=0){
                return ServerResponse.error(ResponseEnum.CART_NUMBER_ERROR);
            }

            //如果不存在就先构建购物车  //如果商品不存在就添加商品,更新购物车
            Cart cart = new Cart();

            List<CartInfo> cartInfoList = cart.getCartInfo();

            //构建商品
            CartInfo cartInfo1 = cartInfoItem(num, product);
           //添加商品
            cart.getCartInfo().add(cartInfo1);
           //更新购物车
            updateCart(memberId, cart);

            return ServerResponse.success(cart);
        }

    }

    @Override
    public ServerResponse findCartList(Long memberId) {

        String cartKey = Keyutil.buildCartKey(memberId);
        String cartJson = RedisUtil.get(cartKey);

        Cart cart = JSONObject.parseObject(cartJson, Cart.class);

        return ServerResponse.success(cart);
    }

    @Override
    public ServerResponse findCartNum(Long memberId) {
        String cartKey = Keyutil.buildCartKey(memberId);
        //从redis中取出key
        String cartJson = RedisUtil.get(cartKey);
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        return ServerResponse.success(cart);
    }

    @Override
    public ServerResponse delGoods(Long memberId, Long goodsId) {
        String cartKey = Keyutil.buildCartKey(memberId);

        String cartJson = RedisUtil.get(cartKey);
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        List<CartInfo> cartInfo1 = cart.getCartInfo();
        for (CartInfo cartInfo : cartInfo1) {
            if(cartInfo.getGoodsId()==goodsId){
                cartInfo1.remove(cartInfo);
                break;
            }
        }
        updateCart(memberId, cart);
        return ServerResponse.success();
    }


    private CartInfo cartInfoItem(int num, Product product) {
        CartInfo cartInfo1 = new CartInfo();
        cartInfo1.setGoodsId(product.getId());
        cartInfo1.setGoodsPrice(product.getPrice());
        cartInfo1.setImagePath(product.getImage());
        cartInfo1.setGoodsName(product.getProductName());
        cartInfo1.setNum(num);
        BigDecimal subPrice = BigDecimalUtil.nul(num + "", product.getPrice().toString());
        cartInfo1.setSubPrice(subPrice);
        return cartInfo1;
    }

    private void updateCart(Long memberId, Cart cart) {
        List<CartInfo> cartInfoList = cart.getCartInfo();
        String cartKey = Keyutil.buildCartKey(memberId);
        int totalCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        if(cartInfoList.size()==0){
            RedisUtil.del(cartKey);
            return;
        }
        for (CartInfo item : cartInfoList) {
            totalCount += item.getNum();
            totalPrice = BigDecimalUtil.add(totalPrice.toString(), item.getSubPrice().toString());
        }
        cart.setTotalNum(totalCount);
        cart.setTotalPrice(totalPrice);
        String cartNewJson = JSONObject.toJSONString(cart);
        RedisUtil.set(cartKey, cartNewJson);
    }
}
