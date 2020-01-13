package com.changgou.common.entity;

/**
 * @author LiXiang
 */
public class R {

    public static Result T(String action) {
        return new Result(true, StatusCode.OK, action + "成功");
    }

    public static Result F(String action) {
        return new Result(false, StatusCode.ERROR, action + "失败");
    }

    public static Result T(String action, Object obj) {

        return new Result(true, StatusCode.OK, action + "成功", obj);
    }

}
