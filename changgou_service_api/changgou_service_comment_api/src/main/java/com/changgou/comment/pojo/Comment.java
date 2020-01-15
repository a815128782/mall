package com.changgou.comment.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Field("userLevel")
    private Integer userLevel; // 会员等级

    @Field("headPic")
    private String headPic; // 用户头像地址

    @Field("spec")
    private Map<String,String> spec; // 商品规格



    @Field("username")
    private String username;  // 评论用户名
    @Field("commentDate")
    private Date commentDate;  // 评论日期

    @Field("commodityScore")
    private Integer commodityScore; // 商品评分
    @Field("images")
    private String images; //图片上传
    @Field("commentText")
    private String commentText;  // 评论内容
    @Field("skuId")
    private String skuId;  // 所评论的商品Id

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Map<String, String> getSpec() {
        return spec;
    }

    public void setSpec(Map<String, String> spec) {
        this.spec = spec;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        this.expressmanAttitude = expressmanAttitude;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
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

    public Integer getCommodityScore() {
        return commodityScore;
    }

    public void setCommodityScore(Integer commodityScore) {
        this.commodityScore = commodityScore;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
