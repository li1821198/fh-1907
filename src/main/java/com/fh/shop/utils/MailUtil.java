package com.fh.shop.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
@Component
public class MailUtil {
          @Value("${spring.mail.host}")
          private String host;
        @Value("${spring.mail.username}")
         private String from;
        @Value("${spring.mail.password}")
         private String password;
        @Value("${spring.mail.protocol}")
         private String protocol;



    public  void DaoMail(String to,String content ,String subject) throws Exception {

        Properties prop = new Properties();
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //2、通过session得到transport对象
        Transport ts = null;

            ts = session.getTransport();
            //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect(host, from, password);
            //4、创建邮件
            Message message = createSimpleMail(session,to,content,subject);
            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();


    }

    /**
     46     * @Method: createSimpleMail
     47     * @Description: 创建一封只包含文本的邮件
     48     * @Anthor:孤傲苍狼
     49     *
     50     * @param session
     51     * @return
     52     * @throws Exception
     53     */

    public  MimeMessage createSimpleMail(Session session,String to,String content ,String subject)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(from));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        //邮件的标题
        message.setSubject(subject);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
}
