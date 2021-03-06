package com.changgou.pay;

import com.changgou.common.interceptor.FeignInterceptor;
import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author LiXiang
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages ={"com.changgou.order.feign"} )
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }
    @Bean
    public WXPay wxPay() {
        try {
            return new WXPay(new MyConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
}
