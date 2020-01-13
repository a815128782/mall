package com.changgou.pay.service.impl;

import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiXiang
 */
@Service
@Slf4j
public class WXPayServiceImpl implements WXPayService {

    @Autowired
    WXPay wxPay;
    @Value("${wxpay.notify_url}")
    String notify_url;

    /**
     * 统一下单的接口调用
     * @param orderId 订单编号
     * @param payMoney 支付金额
     * @return
     */
    @Override
    public Map nativePay(String orderId, String payMoney) {
        try{
            //1.封装请求参数
            Map<String,String> map = new HashMap<>();
            //商品描述
            map.put("body","畅购");
            //商户订单号
            map.put("out_trade_no",orderId);
            //金额 :订单总金额，单位为分
            /*BigDecimal money = new BigDecimal("0.01");
            BigDecimal fen = money.multiply(new BigDecimal("100"));
            fen  = fen.setScale(0, BigDecimal.ROUND_UP);
            map.put("total_fee",fen+"");*/
            map.put("total_fee","1");
            map.put("spbill_create_ip","127.0.0.1");
            map.put("notify_url",notify_url);
            map.put("trade_type","NATIVE");

            //2.基于wxpay完成统一下单接口的调用,并获取返回结果
            Map<String, String> result = wxPay.unifiedOrder(map);
            Map<String,String> resultMap = new HashMap<>();
            if("SUCCESS".equals(result.get("return_code"))&&"SUCCESS".equals(result.get("result_code"))){
                resultMap.put("code_url",result.get("code_url"));
                resultMap.put("orderId",orderId);
                resultMap.put("payMoney",payMoney);
            }
            return resultMap;
        }catch (Exception e){
           e.printStackTrace();
           log.error("WXPayServiceImpl nativePay Exception is {}",e.getMessage());
            return null;
        }
    }

    /**
     * 基于微信关闭订单
     * @param orderId
     * @return
     */
    @Override
    public Map closeOrder(String orderId) {
        try{
            Map<String, String> map = new HashMap<>();
            map.put("out_trade_no",orderId);
            Map<String, String> resultMap = wxPay.closeOrder(map);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 基于微信查询订单
     * @param orderId
     * @return
     */
    @Override
    public Map queryOrder(String orderId) {
        try{
            Map<String,String> map = new HashMap();
            map.put("out_trade_no",orderId);
            Map<String, String> resultMap = wxPay.orderQuery(map);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
