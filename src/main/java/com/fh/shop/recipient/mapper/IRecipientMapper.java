package com.fh.shop.recipient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.recipient.po.Recipient;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IRecipientMapper extends BaseMapper<Recipient> {
}
