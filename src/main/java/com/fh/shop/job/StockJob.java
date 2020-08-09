package com.fh.shop.job;


import com.fh.shop.product.biz.ProductService;
import com.fh.shop.product.po.Product;
import com.fh.shop.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component

public class StockJob {
    @Resource(name = "productService")
    private ProductService productService;
    @Autowired
    private MailUtil mailUtil;

    @Scheduled(cron = "0 0/50 * * * ?")
    public void a(){
        List<Product> list = productService.findProduct();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\">\n" +
                "     <thead>\n" +
                "         <tr>\n" +
                "             <th>商品名</th>\n" +
                "             <th>价格</th>\n" +
                "             <th>库存</th>\n" +
                "         </tr>\n" +
                "     </thead>\n" +
                "    <tbody>"
        );
        for (Product product : list) {
            stringBuffer.append("<tr>\n" +
                    "        <td>"+product.getProductName()+"</td>\n" +
                    "        <td>"+product.getPrice().toString()+"</td>\n" +
                    "        <td>"+product.getStock()+"</td>\n" +
                    "    </tr>");
        }
        stringBuffer.append("</tbody>\n" +
                "</table>");
        String s = stringBuffer.toString();
        try {
            mailUtil.DaoMail("2879442908@qq.com",s,"库存不足");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

