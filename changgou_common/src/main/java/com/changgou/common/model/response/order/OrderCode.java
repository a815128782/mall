package com.changgou.common.model.response.order;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum OrderCode implements ResultCode {

    //brand异常
    ADD_ORDER_ERROR(false,23001,"请先添加商品再结算"),
    USER_LOGIN_ERROR2(false,23002,"用户名或密码错误"),
    ORDER_N_EXIST_ERROR(false,23003,"当前订单不存在"),
    ORDER_LOGISTICS_ERROR(false,23004,"订单物流异常"),
    ORDER_STATUS_ERROR(false,23005,"订单状态异常"),
    ORDER_N_SHIPPED_ERROR(false,23006,"当前订单未发货"),
    ;



    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private OrderCode(boolean success, int code, String message){
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
