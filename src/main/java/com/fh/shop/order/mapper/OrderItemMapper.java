package com.fh.shop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.order.po.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    void addCartItem(List<OrderItem> orderItem);
}
