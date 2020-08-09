package com.fh.shop.member.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_member")
public class Member  implements Serializable {
    private Long id;
    private String memberName;
    private String realName;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date  birthday;
    private String mail;
    private String phone;
    private Long shengId;
    private Long shiId;
    private Long xianId;
    private String areaName;
}
