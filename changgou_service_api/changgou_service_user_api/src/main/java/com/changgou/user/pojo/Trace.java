package com.changgou.user.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LiXiang
 */
public class Trace implements Serializable {

    //到达站
    private String acceptStation;
    //到达时间
    private Date acceptTime;
    //快递状态
    private String remark;

    public String getAcceptStation() {
        return acceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
