package com.fh.shop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//可以注入
@Configuration
public class MQConfig {

    public static final String ORDEREXCHANGE="orderExchange";
    public static final String ORDERQUEUE="orderQueue";
    public static final String ORDERLYKEY="order";

      @Bean
       public DirectExchange  orderExchange(){
           return  new DirectExchange(ORDEREXCHANGE,true,false);
       }
    @Bean
       public Queue  orderQueue(){
           return new Queue(ORDERQUEUE,true,false,false);
       }
    @Bean
       public Binding orderBinding(){
           return   BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDERLYKEY);
       }













    public static final String DIRECTEXCHANGE="goodsExchange" ;
    public static final String QUEUE="goodsQueue" ;
    public static final String LYKEY="goods" ;
  @Bean
  public DirectExchange goodsExchange(){

    return new DirectExchange(DIRECTEXCHANGE,true,false);
  }

  @Bean
  public Queue goodsQueue(){
    return new Queue(QUEUE,true,false,false);
  }

  @Bean
  public Binding goodsBinding(){

    return BindingBuilder.bind(goodsQueue()).to(goodsExchange()).with(LYKEY);
  }

}
