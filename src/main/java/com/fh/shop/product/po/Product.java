package com.fh.shop.product.po;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product implements Serializable {

    private Long id;
    private String productName;
    private BigDecimal price;
    private Long brandId;
    private Date  createDate;
    private Date  insertDate;
    private Date  updateDate;
    private String image;
    private Integer status;
    private Integer isHot;
    private Integer stock;

}
