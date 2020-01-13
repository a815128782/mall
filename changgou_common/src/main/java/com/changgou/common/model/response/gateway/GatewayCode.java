package com.changgou.common.model.response.gateway;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum GatewayCode implements ResultCode {

    //brand异常
    GATEWAY_TOKEN_ERROR(false,27001,"登录令牌已过期或不存在"),
    GATEWAY_AUTHORIZE_ERROR(false,27002,"权限不足,无权操作"),
    GATEWAY_AUTH_LOGIN_ERROR(false,27002,"拒绝访问"),
    ;

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    GatewayCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
