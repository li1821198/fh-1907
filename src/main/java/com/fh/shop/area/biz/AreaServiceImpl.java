package com.fh.shop.area.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.area.mapper.AreaMapper;
import com.fh.shop.area.po.Area;
import com.fh.shop.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    AreaMapper areaMapper;

    @Override
    public ServerResponse findChrds(Long id) {
        QueryWrapper<Area> chrdsList = new QueryWrapper<>();
        chrdsList.eq("fid", id);
        List<Area> areas = areaMapper.selectList(chrdsList);
        return ServerResponse.success(areas);
    }
}
