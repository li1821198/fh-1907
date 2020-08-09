package com.fh.shop.mq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.cart.vo.Cart;
import com.fh.shop.cart.vo.CartInfo;
import com.fh.shop.config.MQConfig;
import com.fh.shop.exception.StockLessException;
import com.fh.shop.order.biz.OrderService;
import com.fh.shop.order.param.OrderParam;
import com.fh.shop.product.mapper.ProductMapper;
import com.fh.shop.product.po.Product;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.MailUtil;
import com.fh.shop.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MQReceiver {
    @Autowired
    MailUtil mailUtil;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderService orderService;
      @RabbitListener(queues = MQConfig.QUEUE )
      public void handMailMessage(String msg, Message message, Channel channel ){

          MailMessage mailMessage = JSONObject.parseObject(msg, MailMessage.class);
          String mail = mailMessage.getTo();
          String content = mailMessage.getContent();
          String total = mailMessage.getTotal();
          try {
              mailUtil.DaoMail(mail,content,total);
              MessageProperties messageProperties = message.getMessageProperties();
              long deliveryTag = messageProperties.getDeliveryTag();
              channel.basicAck(deliveryTag,false);
          } catch (Exception e) {
              e.printStackTrace();
          }

      }
   @RabbitListener(queues = MQConfig.ORDERQUEUE)
      public void handOrderMessage(String msg, Message message, Channel channel) throws IOException {

       MessageProperties messageProperties = message.getMessageProperties();
       long deliveryTag = messageProperties.getDeliveryTag();
       //将json格式的字符串转换为java对象
       OrderParam orderParam = JSONObject.parseObject(msg, OrderParam.class);
        //获取会员Id
       Long memberId = orderParam.getMemberId();
       //获取redis中购物车信息
       String cartJson = RedisUtil.get(Keyutil.buildCartKey(memberId));
       //将json格式的字符串转换为java对象
       Cart cart = JSONObject.parseObject(cartJson, Cart.class);
       if (cart == null){

           channel.basicAck(deliveryTag,false);
           return;
       }
       //购买的商品数量和数据库中对应的商品作比较,发现库存不足,进行提醒
       List<CartInfo> cartInfoList = cart.getCartInfo();
           //通过Lanmda表达式给Id进行比较后返回一个新的集合
        List<Long>    goodsIdList=cartInfoList.stream().map(x->x.getGoodsId()).collect(Collectors.toList());
       QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",goodsIdList);
            //查询商品集合
       List<Product> productList = productMapper.selectList(queryWrapper);
        //循环购物车商品
       for (CartInfo cartInfo : cartInfoList) {
            //循环商品
           for (Product product : productList) {
                //判断购物车中的商品Id和数据库商品Id是否匹配
               if(cartInfo.getGoodsId().longValue()==product.getId().longValue()){
                      //判断当购物车的商品数量大于数据库的商品库存时
                   if (cartInfo.getNum()>product.getStock()){
                       //将提示信息存到redis中
                       RedisUtil.set(Keyutil.buildStockLess(memberId),"stock less");
                        channel.basicAck(deliveryTag,false);
                   return;
               }
               }
           }
       }
       //
       try {
           orderService.createOrder(orderParam);
           channel.basicAck(deliveryTag,false);
       } catch (StockLessException e) {
           e.printStackTrace();
           RedisUtil.set(Keyutil.buildStockLess(memberId),"stock less");
           channel.basicAck(deliveryTag,false);
       }catch (Exception e) {
           e.printStackTrace();
           RedisUtil.set(Keyutil.buildOrderErrorKey(memberId),"error");
           channel.basicAck(deliveryTag,false);

       }
   }

    }

