package com.changgou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author:sss
 * @Date:2020/1/13 15:49
 */
@Table(name="tb_collect")
public class Collect {

    @Id
    private Long id;

    private String username;

    private String sku_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
