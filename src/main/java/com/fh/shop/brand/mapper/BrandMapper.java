package com.fh.shop.brand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.brand.po.Brand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface BrandMapper  extends BaseMapper<Brand> {
    @Insert("insert into t_brand(brandName) values(#{brandName})")
    void addbrand(Brand brand);
    @Select("select b.*,b.brandName brandName from t_brand b")
    List<Brand> queryBrand();
    @Delete("delete from t_brand where id=#{v}")
    void delete(Long id);

    @Update("update t_brand set brandName=#{brandName} where id=#{id}")
    void update(Brand brand);


    void deleteBatch(List<Long> idList);

}
