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

    private String sku_id;

    public Footmark() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkuId() {
        return sku_id;
    }

    public void setSkuId(String sku_id) {
        this.sku_id = sku_id;
    }
}
