package com.changgou.common.model.response.user;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum UserCode implements ResultCode {

    //brand异常
    USER_LOGIN_ERROR(false,29001,"登录异常,请稍后尝试"),
    USER_LOGIN_ERROR2(false,29002,"用户名或密码错误"),
    USER_VALIDATECODE_ERROR(false,29003,"验证码错误"),
    ;

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private UserCode(boolean success, int code, String message){
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
