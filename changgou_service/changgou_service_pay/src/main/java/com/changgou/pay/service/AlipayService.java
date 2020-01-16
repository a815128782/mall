package com.changgou.pay.service;

import java.util.Map;

public interface AlipayService {
    /**
     * 生成二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    public Map createNative(String out_trade_no,String total_fee);
    /**
     * 查询支付订单状态
     * @param out_trade_no
     * @return
     */
    public Map queryPayStatus(String out_trade_no);

    void alipayRefund (String outTradeNo, String tradeNo, String refundAmount, String refundReason, String outRequestNo);
}
