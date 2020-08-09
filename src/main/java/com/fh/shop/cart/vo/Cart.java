package com.fh.shop.cart.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fh.shop.utils.BigDecimalJackson;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class Cart {
     private int totalNum;
     @JsonSerialize(using = BigDecimalJackson.class)
     private BigDecimal totalPrice;
     private List<CartInfo>  cartInfo=new ArrayList<CartInfo>();
}
