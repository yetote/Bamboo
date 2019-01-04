package com.example.bamboo.model;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class describe
 * @time 2018/11/6 15:06
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoBean {
    /**
     * 背景图片，
     * 标签，
     * 作者，
     * 类别，
     * 主题，
     * 标题，
     * 内容
     */
    private String videoBcImage, videoTag, videoWriter, videoCategory, videoTopic, videoTitle, videoContent;


    /**
     * 播放数
     * 评论数
     * 收藏数
     * 视频时长
     * 点赞数量
     */
    private int videoPlayNum, videoDiscussNum, videoCollectionNum, videoTime, videoPriseNum;
    int id;

    public RecommendVideoBean(String videoBcImage, String videoTag, String videoWriter, String videoCategory, String videoTopic, String videoTitle, String videoContent, int videoPlayNum, int videoDiscussNum, int videoCollectionNum, int videoTime, int videoPriseNum, int id) {
        this.videoBcImage = videoBcImage;
        this.videoTag = videoTag;
        this.videoWriter = videoWriter;
        this.videoCategory = videoCategory;
        this.videoTopic = videoTopic;
        this.videoTitle = videoTitle;
        this.videoContent = videoContent;
        this.videoPlayNum = videoPlayNum;
        this.videoDiscussNum = videoDiscussNum;
        this.videoCollectionNum = videoCollectionNum;
        this.videoTime = videoTime;
        this.videoPriseNum = videoPriseNum;
        this.id = id;
    }

    public String getVideoBcImage() {
        return videoBcImage;
    }

    public void setVideoBcImage(String videoBcImage) {
        this.videoBcImage = videoBcImage;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public String getVideoWriter() {
        return videoWriter;
    }

    public void setVideoWriter(String videoWriter) {
        this.videoWriter = videoWriter;
    }

    public String getVideoCategory() {
        return videoCategory;
    }

    public void setVideoCategory(String videoCategory) {
        this.videoCategory = videoCategory;
    }

    public String getVideoTopic() {
        return videoTopic;
    }

    public void setVideoTopic(String videoTopic) {
        this.videoTopic = videoTopic;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoContent() {
        return videoContent;
    }

    public void setVideoContent(String videoContent) {
        this.videoContent = videoContent;
    }

    public int getVideoPlayNum() {
        return videoPlayNum;
    }

    public void setVideoPlayNum(int videoPlayNum) {
        this.videoPlayNum = videoPlayNum;
    }

    public int getVideoDiscussNum() {
        return videoDiscussNum;
    }

    public void setVideoDiscussNum(int videoDiscussNum) {
        this.videoDiscussNum = videoDiscussNum;
    }

    public int getVideoCollectionNum() {
        return videoCollectionNum;
    }

    public void setVideoCollectionNum(int videoCollectionNum) {
        this.videoCollectionNum = videoCollectionNum;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public int getVideoPriseNum() {
        return videoPriseNum;
    }

    public void setVideoPriseNum(int videoPriseNum) {
        this.videoPriseNum = videoPriseNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RecommendVideoBean{" +
                "videoBcImage='" + videoBcImage + '\'' +
                ", videoTag='" + videoTag + '\'' +
                ", videoWriter='" + videoWriter + '\'' +
                ", videoCategory='" + videoCategory + '\'' +
                ", videoTopic='" + videoTopic + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoContent='" + videoContent + '\'' +
                ", videoPlayNum=" + videoPlayNum +
                ", videoDiscussNum=" + videoDiscussNum +
                ", videoCollectionNum=" + videoCollectionNum +
                ", videoTime=" + videoTime +
                ", videoPriseNum=" + videoPriseNum +
                ", id=" + id +
                '}';
    }
}
