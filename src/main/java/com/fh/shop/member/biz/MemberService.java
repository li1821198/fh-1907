package com.fh.shop.member.biz;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.po.Member;

public interface MemberService {

     ServerResponse add(Member member) throws Exception;

     ServerResponse validaterMemName(String member);
     ServerResponse validaterEmail(String mail);
     ServerResponse validaterPhone(String phone);

    ServerResponse login(String userName, String password);


}
