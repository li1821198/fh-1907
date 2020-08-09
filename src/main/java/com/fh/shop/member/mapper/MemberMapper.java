package com.fh.shop.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.member.po.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member> {
    @Insert("insert into t_member (memberName,password,realName,mail,phone,birthday,shengId,shiId,xianId,areaName) values (#{memberName},#{password},#{realName},#{mail},#{phone},#{birthday},#{shengId},#{shiId},#{xianId},#{areaName})" )
    void add(Member member);
}
