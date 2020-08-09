package com.fh.shop.mq;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.config.MQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//启动扫描
@Component
public class MQSender {
  @Autowired
  private AmqpTemplate amqpTemplate;

  public void  sendGoodsMessage(String info){

    amqpTemplate.convertAndSend(MQConfig.DIRECTEXCHANGE,MQConfig.LYKEY,"你好"+info);
  }
  public void  sendGoodsMessage(MailMessage mailMessage){
    String mailJson = JSONObject.toJSONString(mailMessage);


    amqpTemplate.convertAndSend(MQConfig.DIRECTEXCHANGE,MQConfig.LYKEY,mailJson);
  }
}
