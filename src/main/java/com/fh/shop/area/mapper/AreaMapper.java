package com.fh.shop.area.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.area.po.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AreaMapper extends BaseMapper<Area> {
}
