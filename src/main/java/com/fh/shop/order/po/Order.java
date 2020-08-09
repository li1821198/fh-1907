package com.fh.shop.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order  implements Serializable {
      //字符串全局唯一
      @TableId(value = "id",type = IdType.INPUT)
      private String id;
      private Long userId;
      private Date createTime;
      private int status;
      private BigDecimal totalPrice;
      private int  totalNum;
      private int  payType;
      private Date  payTime;
      private Long recipientId;
      private String recipientor;
      private String address;
      private String  phone;
      private String  mail;
}
