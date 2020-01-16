package com.changgou.user.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author LiXiang
 */
public class TraceVo implements Serializable {

    private List<Trace> traceList;

    private String orderId;

    private String shipperCode;

    public List<Trace> getTraceList() {
        return traceList;
    }

    public void setTraceList(List<Trace> traceList) {
        this.traceList = traceList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }
}
