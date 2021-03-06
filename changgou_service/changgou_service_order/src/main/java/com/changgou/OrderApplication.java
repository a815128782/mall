package com.changgou;

import com.changgou.common.interceptor.FeignInterceptor;
import com.changgou.common.util.IdWorker;
import com.changgou.order.config.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling //开启定时任务
@EnableFeignClients(basePackages = {"com.changgou.pay.feign","com.changgou.goods.feign","com.changgou.user.feign"})
@MapperScan(basePackages = {"com.changgou.order.dao"})
@ComponentScan(basePackages = {"com.changgou.order","com.changgou.common"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run( OrderApplication.class);
    }


    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1,1);
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
