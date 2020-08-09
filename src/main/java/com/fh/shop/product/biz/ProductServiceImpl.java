package com.fh.shop.product.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.product.mapper.ProductMapper;
import com.fh.shop.product.po.Product;
import com.fh.shop.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse findList() {
        String hotProductList = RedisUtil.get("hotProductList");
        if (StringUtils.isNotEmpty(hotProductList)){
            List<Product> productList = JSONObject.parseArray(hotProductList, Product.class);
            return ServerResponse.success(productList);
        }
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("isHot", 1);
        productQueryWrapper.eq("status", 1);
        List<Product> products = productMapper.selectList(productQueryWrapper);
        String productsJson = JSONObject.toJSONString(products);
          RedisUtil.set("hotProductList",productsJson);


        return ServerResponse.success(products);
    }

    @Override
    public List<Product> findProduct() {
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.lt("stock", 10);
        List<Product> products = productMapper.selectList(productQueryWrapper);
        return products;
    }


}
