package com.changgou.common.model.response.system;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum SystemCode implements ResultCode {

    //admin
    SYSTEM_ADD_ADMIN_ERROR(false,26001,"用户名和密码不能为空"),
    SYSTEM_ADD_USERNAME_ERROR(false,26002,"用户名已存在"),
    SYSTEM_LOGIN_USERNAME_ERROR(false,26003,"请输入用户名"),
    SYSTEM_LOGIN_PASSWORD_ERROR(false,26006,"请输入密码"),
    SYSTEM_LOGIN_ERROR(false,26005,"用户名或密码不正确");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private SystemCode(boolean success, int code, String message){
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
