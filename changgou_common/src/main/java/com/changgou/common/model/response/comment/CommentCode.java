package com.changgou.common.model.response.comment;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

/**
 * 30000
 */
@ToString
public enum CommentCode implements ResultCode {

    //brand异常
    ORDERITEM_BLANK_ERROR(false,30001,"订单内无商品异常"),
    SKUID_BLANK_ERROR(false,30002,"订单内无商品异常");


    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CommentCode(boolean success, int code, String message){
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
