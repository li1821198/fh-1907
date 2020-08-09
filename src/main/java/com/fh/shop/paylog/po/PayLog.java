package com.fh.shop.paylog.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_paylog")
public class PayLog implements Serializable {
      @TableId(type = IdType.INPUT,value = "out_trade_no")
    private String outTradeNo;
      private Long userId;
      private String orderId;
      private Date createTime;
      private Date payTime;
      private BigDecimal payMoney;
      private int  payType;
      private int payStatus;
      private String transactionId;
}
