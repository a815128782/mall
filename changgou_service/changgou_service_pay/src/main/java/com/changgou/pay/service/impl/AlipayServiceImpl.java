package com.changgou.pay.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
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
        return null;
    }
}
