package com.changgou.comment.pojo;

/**
 * @Author:Administrator
 * @Date: 2020/1/13 21:21
 */
public class SkuComment {


    private Integer commodityScore; // 商品评分


    private String images; //图片上传

    private String commentText;  // 评论内容

    private String skuId;  // 所评论的商品Id

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
