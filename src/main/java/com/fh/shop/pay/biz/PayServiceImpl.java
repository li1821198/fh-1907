package com.fh.shop.pay.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.config.WxConfig;
import com.fh.shop.order.mapper.OrderMapper;
import com.fh.shop.order.po.Order;
import com.fh.shop.paylog.mapper.PayLogMapper;
import com.fh.shop.paylog.po.PayLog;
import com.fh.shop.utils.BigDecimalUtil;
import com.fh.shop.utils.DateUtil;
import com.fh.shop.utils.Keyutil;
import com.fh.shop.utils.RedisUtil;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
       @Autowired
    PayLogMapper payLogMapper;
       @Autowired
    OrderMapper orderMapper;

    @Override
    public ServerResponse createNative(Long memberId) {
             //获取会员对应的支付日志
        String payLogJson = RedisUtil.get(Keyutil.buildPayLogKey(memberId));
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
        String outTradeNo = payLog.getOutTradeNo();
        BigDecimal payMoney = payLog.getPayMoney();
        String orderId = payLog.getOrderId();

        try {
            WxConfig config = new WxConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "飞狐乐购");
        data.put("out_trade_no",outTradeNo);
            int money = BigDecimalUtil.nul(payMoney.toString(), "100").intValue();
            data.put("total_fee", money+"");
           data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付

            String time = DateUtil.addMinutes(new Date(), 2, DateUtil.FULL_TIMEINFO);
            data.put("time_expire",time);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if (!return_code.equals("SUCCESS")) {
                 return ServerResponse.error(99999,return_msg);
            }
            String result_code = resp.get("result_code");
            String error_code_des = resp.get("error_code_des");
            if (!result_code.equals("SUCCESS")) {
                return ServerResponse.error(99999,error_code_des);
            }
            String code_url = resp.get("code_url");
            Map<String, String> resultMap =new HashMap<>();
            resultMap.put("code_url",code_url);
            resultMap.put("orderId",orderId);
            resultMap.put("totalPrice",payMoney.toString());
            return ServerResponse.success(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }


    }

    @Override
    public ServerResponse queryStatus(Long memberId) {
        WxConfig wxConfig = new WxConfig();
        try {
            String payLogJson = RedisUtil.get(Keyutil.buildPayLogKey(memberId));
            PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
            String orderId = payLog.getOrderId();
            String outTradeNo = payLog.getOutTradeNo();
            WXPay wxpay = new WXPay(wxConfig);
            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no",outTradeNo);

            int count =0;
            while (true){
            Map<String, String> resp = wxpay.orderQuery(data);

            System.out.println(resp);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");

            if (!return_code.equals("SUCCESS")) {
                return ServerResponse.error(99999,return_msg);
            }
            String result_code = resp.get("result_code");
            String error_code_des = resp.get("error_code_des");
            if (!result_code.equals("SUCCESS")) {
                return ServerResponse.error(99999,error_code_des);
            }
            String trade_state = resp.get("trade_state");
            if (trade_state.equals("SUCCESS")) {
                String transaction_id = resp.get("transaction_id");
                 //更新订单
                Order order = new Order();
                  order.setId(orderId);
                  order.setPayTime(new Date());
                  order.setStatus(SystemConstant.OrderStatus.PAY_SUCCESS);
                   orderMapper.updateById(order);
                 //更新支付日志
                    PayLog payLogInfo =new PayLog();
                    payLogInfo.setOutTradeNo(outTradeNo);
                    payLogInfo.setPayTime(new Date());
                    payLogInfo.setPayStatus(SystemConstant.PayStatus.PAY_SUCCESS);
                    payLogInfo.setTransactionId(transaction_id);
                    payLogMapper.updateById(payLogInfo);
                    //删除Redis中的日志记录
                    RedisUtil.del(Keyutil.buildPayLogKey(memberId));
                    return ServerResponse.success();
            }else {
                Thread.sleep(2000);
                count++;
                if (count >60) {
                    return ServerResponse.error(ResponseEnum.PAY_IS_ERROR);
                }

            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  ServerResponse.error();
        }


    }
}
