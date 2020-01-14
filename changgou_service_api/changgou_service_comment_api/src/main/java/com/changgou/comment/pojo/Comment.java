package com.changgou.comment.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2020/1/13 14:56
 */
@Document(collection = "comment")
public class Comment implements Serializable {
    @Id
    private String id;  // 评论id

    @Field("orderId")
    private String orderId; //订单id

    @Field("productConformity")
    private Integer productConformity; //商品符合度

    @Field("salerAttitude")
    private Integer salerAttitude; // 商家服务态度

    @Field("deliverySpeed")
    private Integer deliverySpeed; // 物流发货速度

    @Field("ExpressmanAttitude")
    private Integer expressmanAttitude; // 快递员态度

    @Field("anonymous")
    private boolean anonymous; // 是否匿名

    @Field("username")
    private String username;  // 评论用户名
    @Field("commentDate")
    private Date commentDate;  // 评论日期

    @Field("skuCommentList")
    private List<SkuComment> skuCommentList;  // 每件商品的评价

    public List<SkuComment> getSkuCommentList() {
        return skuCommentList;
    }

    public void setSkuCommentList(List<SkuComment> skuCommentList) {
        this.skuCommentList = skuCommentList;
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getProductConformity() {
        return productConformity;
    }

    public void setProductConformity(Integer productConformity) {
        this.productConformity = productConformity;
    }

    public Integer getSalerAttitude() {
        return salerAttitude;
    }

    public void setSalerAttitude(Integer salerAttitude) {
        this.salerAttitude = salerAttitude;
    }

    public Integer getDeliverySpeed() {
        return deliverySpeed;
    }

    public void setDeliverySpeed(Integer deliverySpeed) {
        this.deliverySpeed = deliverySpeed;
    }

    public Integer getExpressmanAttitude() {
        return expressmanAttitude;
    }

    public void setExpressmanAttitude(Integer expressmanAttitude) {
        expressmanAttitude = expressmanAttitude;
    }


    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }



}
