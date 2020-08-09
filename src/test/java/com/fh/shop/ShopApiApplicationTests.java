package com.fh.shop;


import com.fh.shop.mq.MQSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopApiApplicationTests {
    /*@Autowired
    JavaMailSenderImpl mailSender;*/
    @Autowired
    private MQSender mqSender;
    @Test
    void contextLoads() {
     /*   SimpleMailMessage mailMessage = new SimpleMailMessage();
        //主题
        mailMessage.setSubject("1907A-李毅杰");
        //内容
        mailMessage.setText("撒打算洒");
        //收件人邮箱地址
        mailMessage.setTo("1991737677@qq.com");
        //发件人邮箱地址
        mailMessage.setFrom("3452603469@qq.com");
        mailSender.send(mailMessage);*/

          mqSender.sendGoodsMessage("nihao");
        }

    }




