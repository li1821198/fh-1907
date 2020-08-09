package com.fh.shop.product.controller;

import com.fh.shop.annotation.Check;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.product.biz.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
@Api(tags = "商品接口")
public class ProductController {
         @Autowired
         private ProductService productService;

  @GetMapping
  @Check
  @ApiOperation("查询商品")
    public ServerResponse findList(){
      return  productService.findList();


  }
}
