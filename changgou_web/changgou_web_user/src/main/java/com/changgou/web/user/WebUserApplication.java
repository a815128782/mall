package com.changgou.web.user;

import com.changgou.common.interceptor.FeignInterceptor;
import com.changgou.web.user.config.KdniaoTrackQueryAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.changgou.user.feign","com.changgou.order.feign","com.changgou.pay.feign"})
public class WebUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebUserApplication.class, args);
    }

    @Bean
    public FeignInterceptor feignInterceptor() {

        return new FeignInterceptor();
    }

    @Bean
    public KdniaoTrackQueryAPI kdniaoTrackQueryAPI() {
        return new KdniaoTrackQueryAPI();
    }


}
