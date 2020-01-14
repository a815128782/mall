package com.changgou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author:sss
 * @Date:2020/1/13 15:49
 */
@Table(name="tb_footmark")
public class Footmark {

    @Id
    private String username;

    private String skuId;

    public Footmark() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
