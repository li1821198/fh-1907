package com.fh.shop.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shop.cart.vo.Cart;
import com.fh.shop.cart.vo.CartInfo;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.config.MQConfig;
import com.fh.shop.exception.StockLessException;
import com.fh.shop.order.mapper.OrderItemMapper;
import com.fh.shop.order.mapper.OrderMapper;
import com.fh.shop.order.param.OrderParam;
import com.fh.shop.order.po.Order;
import com.fh.shop.order.po.OrderItem;
import com.fh.shop.order.vo.OrderConfigVo;
import com.fh.shop.paylog.mapper.PayLogMapper;
import com.fh.shop.paylog.po.PayLog;
import com.fh.shop.product.mapper.ProductMapper;
import com.fh.shop.recipient.biz.IRecipientService;
import com.fh.shop.recipient.mapper.IRecipientMapper;
import com.fh.shop.recipient.po.Recipient;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.RedisUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
      @Autowired
      private IRecipientService recipientService;
      @Autowired
      private RabbitTemplate rabbitTemplate;
      @Autowired
      private ProductMapper productMapper;
      @Autowired
      private IRecipientMapper recipientMapper;
      @Autowired
      private  OrderMapper orderMapper;
      @Autowired
      private OrderItemMapper orderItemMapper;
      @Autowired
      private PayLogMapper payLogMapper;
    @Override
    public ServerResponse generateOrderConfirm(Long memberId) {
        List<Recipient> recipientList = recipientService.findList(memberId);
        String cartJson = RedisUtil.get(Keyutil.buildCartKey(memberId));
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);

        OrderConfigVo orderConfigVo = new OrderConfigVo();
        orderConfigVo.setCart(cart);
        orderConfigVo.setRecipientList(recipientList);
        return ServerResponse.success(orderConfigVo);
    }

    @Override
    public ServerResponse generateOrder(OrderParam orderParam) {
        Long memberId = orderParam.getMemberId();
        RedisUtil.del(Keyutil.buildStockLess(memberId));
        RedisUtil.del(Keyutil.buildOrderKey(memberId));
        RedisUtil.del(Keyutil.buildPayLogKey(memberId));
        String orderJson = JSONObject.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConfig.ORDEREXCHANGE,MQConfig.ORDERLYKEY,orderJson);
        return ServerResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(OrderParam orderParam) {
         //获取会员Id
        Long memberId = orderParam.getMemberId();
         //从Redis中取出购物车
        String cartJson = RedisUtil.get(Keyutil.buildCartKey(memberId));
         //将JSON格式的购物车转换为Java对象
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
         //获取购物车商品集合
        List<CartInfo> cartInfoList = cart.getCartInfo();
        //循环购物车商品
        for (CartInfo cartInfo : cartInfoList) {
             //获取购物车商品Id和商品数量
            Long goodsId = cartInfo.getGoodsId();
            int num = cartInfo.getNum();
            //将商品Id和商品数量作为参数调用修改商品的方法并返回一个收影响的行数
            int rowCount = productMapper.updateStock(goodsId, num);
             //当受影响的行数==0时就抛出异常并返回错误信息
            if(rowCount ==0){
              throw new StockLessException("stock less");

            }
        }
        //通过收件人Id获取整个收件人对象
        Long recipientId = orderParam.getRecipientId();
        Recipient recipient = recipientMapper.selectById(recipientId);
        //创建一个订单对象
        Order order = new Order();
        //通过雪花算法生成一个订单Id
        String orderId = IdWorker.getIdStr();
         //给订单Id赋值
        //给订单对象赋值
        order.setId(orderId);
        order.setCreateTime(new Date());
        order.setRecipientId(recipientId);
        order.setRecipientor(recipient.getRecipientor());
        order.setMail(recipient.getMail());
        order.setPhone(recipient.getPhone());
        order.setAddress(recipient.getAddress());
        order.setTotalNum(cart.getTotalNum());
        BigDecimal totalPrice = cart.getTotalPrice();
        order.setTotalPrice(totalPrice);
        order.setUserId(memberId);
        order.setStatus(SystemConstant.OrderStatus.WAIT_PAY);
        int payType = orderParam.getPayType();
        order.setPayType(payType);
        orderMapper.insert(order);
        //订单明细表
            List<OrderItem> orderItems = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
             orderItem.setOrderId(orderId);
             orderItem.setImagePath(cartInfo.getImagePath());
             orderItem.setNum(cartInfo.getNum());
             orderItem.setPrice(cartInfo.getGoodsPrice());
             orderItem.setProductId(cartInfo.getGoodsId());
              orderItem.setProductName(cartInfo.getGoodsName());
              orderItem.setSubPrice(cartInfo.getSubPrice());
              orderItem.setUserId(memberId);
            orderItems.add(orderItem);

        }

        orderItemMapper.addCartItem(orderItems);
        //插入支付日志表
        PayLog payLog = new PayLog();
        String payId = IdWorker.getIdStr();
        payLog.setOutTradeNo(payId);
        payLog.setCreateTime(new Date());
        payLog.setPayMoney(totalPrice);
        payLog.setOrderId(orderId);
        payLog.setPayType(payType);
        payLog.setUserId(memberId);
        payLog.setPayStatus(SystemConstant.PayStatus.WAIT_PAY);
        payLogMapper.insert(payLog);
        String payLogJson = JSONObject.toJSONString(payLog);
        RedisUtil.set(Keyutil.buildPayLogKey(memberId),payLogJson);
        RedisUtil.del(Keyutil.buildCartKey(memberId));
        RedisUtil.set(Keyutil.buildOrderKey(memberId),"OK");
    }

    @Override
    public ServerResponse getResult(Long memberId) {
              if (RedisUtil.exist(Keyutil.buildStockLess(memberId))){
                  RedisUtil.del(Keyutil.buildStockLess(memberId));
                  return  ServerResponse.error(ResponseEnum.ORDER_STOCK_LESS);
              }
              if (RedisUtil.exist(Keyutil.buildOrderKey(memberId))){
                  RedisUtil.del(Keyutil.buildOrderKey(memberId));
                  return ServerResponse.success();
              }
              if (RedisUtil.exist(Keyutil.buildOrderErrorKey(memberId))){
                  RedisUtil.del(Keyutil.buildOrderErrorKey(memberId));
                   return ServerResponse.error(ResponseEnum.ORDER_Error);
              }
              return ServerResponse.error(ResponseEnum.ORDER_QUEUE);


    }
}
