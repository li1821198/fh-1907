package com.fh.shop.recipient.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class Recipient implements Serializable {

    private Long id;
    private String recipientor;
    private String address;
    private String phone;
    private String  mail;
    private Long memberId;
    private int status;
}
