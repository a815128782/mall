package com.changgou.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.changgou.pay.service.AlipayService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService{
    private static Log  log = LogFactory.getLog(AlipayServiceImpl.class);
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //当面付生成二维码
        String outTradeNo=out_trade_no;
        //描述
        String subject="畅购";
        //总金额
        //String totalAmount="0.01";
        String totalAmount=Double.valueOf(total_fee)/100+"";
        //打折金额
        String undiscountableAmount="";

        String sellerId = "";
        //订单描述
        String body="畅购扫码支付"+Double.valueOf(total_fee)/100+"元";
        //操作员编号
        String operatorId="test_operator_id";
        //商户编号
        String storeId="test_store_id";
        //业务扩展

        ExtendParams extendParams=new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        //支付超时时间
        String timeoutExpress="120m";
        //商品明细
        List<com.alipay.demo.trade.model.GoodsDetail> goodsDetailList=new ArrayList<>();
        //创建一个商品信息
        GoodsDetail goods1= GoodsDetail.newInstance(out_trade_no,"畅购",Long.valueOf(total_fee),1);

        //创建扫码支付请求
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl("http://jizhi.utools.club/wxpay/alipayCallBack")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        Configs.init("zfbinfo.properties");

        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result=tradeService.tradePrecreate(builder);
        Map map=new HashMap();
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
//                String filePath = String.format("/Users/sudo/Desktop/qr-%s.png",
//                    response.getOutTradeNo());
//                log.info("filePath:" + filePath);
                //                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                map.put("code_url", response.getQrCode());//生成支付二维码的链接
                map.put("out_trade_no", out_trade_no);
                map.put("total_fee", total_fee);
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return map;
    }
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        // (必填) 商户订单号，通过此商户订单号查询当面付的交易状态
        String outTradeNo = out_trade_no;

        // 创建查询请求builder，设置请求参数
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(outTradeNo);
        Configs.init("zfbinfo.properties");

        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
        //判断支付状态
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("查询返回该订单支付成功: )");

                AlipayTradeQueryResponse response = result.getResponse();
                dumpResponse(response);

                log.info(response.getTradeStatus());
                Map map=new HashMap();
                map.put("tradeStatus", response.getTradeStatus());
                map.put("totalAmount", response.getTotalAmount());
                map.put("tradeNo", response.getTradeNo());
                return map;
            case FAILED:
                log.error("查询返回该订单支付失败或被关闭!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单支付状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return null;
    }
/**
 * 支付宝订单退款
 */
    public void alipayRefund(String outTradeNo, String tradeNo, String refundAmount, String refundReason, String outRequestNo) {
        /*log.info("支付宝退款开始---------");
        AlipayClient alipayClient = this.getAliPayClient();
        //订单编号,支付宝交易号不为空
        if (outTradeNo != null && tradeNo != null) {
            AlipayTradeRefundRequest aliPayRequest = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(outTradeNo);
            model.setTradeNo(tradeNo);
            model.setRefundAmount(refundAmount);
            model.setRefundReason(refundReason);
            model.setOutRequestNo(outRequestNo);
            aliPayRequest.setBizModel(model);
            try {
                AlipayTradeRefundResponse aliPayResponse = alipayClient.execute(aliPayRequest);
                log.debug("aliPayResponse:{}", aliPayResponse);
                if (!"10000".equals(aliPayResponse.getCode())) {
                    log.info("支付宝退款失败，支付宝交易号：{+"+tradeNo+"+}");
                    throw new RuntimeException("退款失败");
                }
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * 获取支付宝Client
     *
     * @return
     */
    /*private AlipayClient getAliPayClient() {
        return new DefaultAlipayClient(
                AliPayProperties.GATEWAY_URL, AliPayProperties.APP_ID, AliPayProperties.PRIVATE_KEY, AliPayProperties.FORMAT,
                AliPayProperties.CHARSET, AliPayProperties.PUBLIC_KEY, AliPayProperties.SIGN_TYPE);
    }*/
}
