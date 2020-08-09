package com.fh.shop.paylog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.paylog.po.PayLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PayLogMapper  extends BaseMapper<PayLog> {
}
