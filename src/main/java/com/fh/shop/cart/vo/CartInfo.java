package com.fh.shop.cart.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fh.shop.utils.BigDecimalJackson;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartInfo {
       private Long goodsId;
       private String goodsName;
       @JsonSerialize(using = BigDecimalJackson.class)
       private BigDecimal goodsPrice;
       private  int  num;
       @JsonSerialize(using = BigDecimalJackson.class)
       private  BigDecimal subPrice;
       private String imagePath;
  }
