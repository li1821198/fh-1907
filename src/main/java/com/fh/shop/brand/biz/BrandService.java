package com.fh.shop.brand.biz;

import com.fh.shop.brand.po.Brand;
import com.fh.shop.common.ServerResponse;

import java.util.List;

public interface BrandService {
    ServerResponse addBrand(Brand brand);

    List<Brand> queryBrand();
    ServerResponse delete(Long id);

    ServerResponse update(Brand brand);

    ServerResponse deleteBatch(String ids);
}
