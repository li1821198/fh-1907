package com.fh.shop.category.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.category.po.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
@Mapper
@Repository
public interface CategoryMapper extends BaseMapper<Category> {



}
