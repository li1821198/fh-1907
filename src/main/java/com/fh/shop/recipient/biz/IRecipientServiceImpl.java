package com.fh.shop.recipient.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.recipient.mapper.IRecipientMapper;
import com.fh.shop.recipient.po.Recipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IRecipientServiceImpl implements IRecipientService {
    @Autowired
    IRecipientMapper recipientMapper;
    @Override
    public List<Recipient> findList(Long memberId) {


        QueryWrapper<Recipient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberId",memberId);

        return recipientMapper.selectList(queryWrapper);
    }
}
