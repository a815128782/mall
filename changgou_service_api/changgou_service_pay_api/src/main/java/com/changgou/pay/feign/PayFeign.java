package com.changgou.pay.feign;

import com.changgou.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiXiang
 */
@FeignClient(name = "pay")
public interface PayFeign {

    /**
     * 微信支付
     * @param orderId
     * @param payMoney
     * @return
     */
    @GetMapping("/wxpay/nativePay")
    public Result nativePay(@RequestParam("orderId") String orderId, @RequestParam("payMoney")String payMoney);
    /**
     * 基于微信查询订单
     * @param orderId
     * @return
     */
    @GetMapping("/wxpay/query/{orderId}")
    public Result queryOrder(@PathVariable("orderId")String orderId);


    /**
     * 基于微信关闭订单
     * @param orderId
     * @return
     */
    @PutMapping("/wxpay/close/{orderId}")
    public Result closeOrder(@PathVariable("orderId")String orderId);

    @RequestMapping("/wxpay/createAlipayNative")
    public Result createAlipayNative(@RequestParam("orderId") String orderId, @RequestParam("payMoney") String payMoney);
}
