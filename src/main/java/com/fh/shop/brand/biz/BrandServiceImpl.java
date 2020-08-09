package com.fh.shop.brand.biz;

import com.fh.shop.brand.po.Brand;
import com.fh.shop.brand.mapper.BrandMapper;
import com.fh.shop.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class BrandServiceImpl implements com.fh.shop.brand.biz.BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public ServerResponse addBrand(Brand brand) {
        brandMapper.addbrand(brand);
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> queryBrand() {
        return brandMapper.queryBrand();
    }

    @Override
    public ServerResponse delete(Long id) {
        brandMapper.delete(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse update(Brand brand) {
        brandMapper.update(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatch(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArr = ids.split(",");
            List<Long> idList = Arrays.stream(idArr).map(x -> Long.parseLong(x)).collect(Collectors.toList());
            brandMapper.deleteBatch(idList);
        }
        return ServerResponse.success();
    }
}
