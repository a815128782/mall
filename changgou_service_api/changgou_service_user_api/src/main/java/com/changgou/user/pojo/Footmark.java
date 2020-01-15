package com.changgou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author:sss
 * @Date:2020/1/13 15:49
 */
@Table(name="tb_footmark")
public class Footmark {

    @Id
    private String username;

    private String sku_id;

    private Date create_time;

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }


    public Footmark() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
