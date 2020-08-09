package com.fh.shop.product.biz;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.product.po.Product;
import java.util.List;

public interface ProductService {
    ServerResponse findList();
    List<Product> findProduct();
}
