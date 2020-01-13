package com.changgou.common.model.response.business;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

//25000-- 文件系统错误代码
@ToString
public enum BusinessCode implements ResultCode {


    AD_CACHE_UPDATE_FAILED(false,28001,"广告缓存更新失败");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private BusinessCode(boolean success, int code, String message){
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
