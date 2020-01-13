package com.changgou.business.listener;

import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.business.BusinessCode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author LiXiang
 */
@Component
public class AdListener {

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues = "ad_update_queue")
    public void receiveMessage(String message){
        System.out.println("接收到的消息为: "+message);
        String url = "http://192.168.200.128/ad_update?position="+message;
        try {
            Map resultMap = restTemplate.getForObject(url, Map.class);
            System.out.println("请求成功");
        } catch (RestClientException e) {
            ExceptionCast.cast(BusinessCode.AD_CACHE_UPDATE_FAILED);
        }

        //发起远程调用
        /*OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
//                e.printStackTrace();
                ExceptionCast.cast(BusinessCode.AD_CACHE_UPDATE_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功

                System.out.println("请求成功: "+response.message());
            }
        });*/
    }


}
