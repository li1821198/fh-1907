package com.fh.shop.mq;

import lombok.Data;

@Data
public class MailMessage  {
     private String realName;
     private String  total;
     private String content;
     private String  to;
}
