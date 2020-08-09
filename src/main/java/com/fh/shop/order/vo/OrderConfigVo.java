package com.fh.shop.order.vo;

import com.fh.shop.cart.vo.Cart;
import com.fh.shop.recipient.po.Recipient;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class OrderConfigVo implements Serializable {
 private    List<Recipient> recipientList =new ArrayList<>();
 private       Cart cart;
}
