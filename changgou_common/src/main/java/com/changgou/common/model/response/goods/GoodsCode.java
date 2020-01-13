package com.changgou.common.model.response.goods;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum GoodsCode implements ResultCode {

    //brand异常
    GOODS_BRAND_ADD_ERROR(false,22001,"商品添加失败"),
    //spu异常
    GOODS_NE_ERROR(false,22002,"当前商品不存在"),
    GOODS_CHECK_ERROR(false,22003,"当前商品未审核"),
    GOODS_PULL_ERROR(false,22004,"当前商品必须处于下架状态才能删除"),
    GOODS_RESTOR_ERROR(false,22005,"当前商品处于未被删除状态"),
    GOODS_DELETE_ERROR(false,22006,"当前商品已删除"),
    GOODS_SPU_SPEC_ERROR(false,22007,"商品规格不能为空"),
    //sku异常
    GOODS_SKU_FIND_ERROR(false,22008,"当前未查询到商品数据,无法导入索引库"),
    GOODS_SKU_NUM_ERROR(false,22009,"库存不足,请重试"),
    ;


    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private GoodsCode(boolean success, int code, String message){
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
