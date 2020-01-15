package com.changgou.web.gateway.filter;

/**
 * @author LiXiang
 */
public class UrlFilter {

    //所有需要传递令牌的地址
    public static String filterPath="/api/wcenter/getUsername,/api/wcollect/**,/api/worder/**,/api/wseckillorder,/api/seckill,/api/wxpay,/api/wxpay/**,/api/worder/**,/api/user/**,/api/address/**,/api/wcart/**,/api/cart/**,/api/categoryReport/**,/api/orderConfig/**,/api/order/**,/api/orderItem/**,/api/orderLog/**,/api/preferential/**,/api/returnCause/**,/api/returnOrder/**,/api/returnOrderItem/**,/api/wcenter/**,/api/wuser/user";

    public static boolean hasAuthorize(String url){
        String[] split = filterPath.replace("**", "").split(",");
        for (String value : split) {
            if(url.startsWith(value)){
                return true;//当前的地址是需要传递令牌的
            }
        }
        return false;
    }
}
