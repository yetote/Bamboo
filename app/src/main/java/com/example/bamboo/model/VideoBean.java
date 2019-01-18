package com.example.bamboo.model;

public class VideoBean {
    private String videoTag, videoWriter, videoCategory, videoTopic, videoTitle, videoContent, videoBcImage;
    private long videoUpTime;
    private int videoPlayNum, videoDiscussNum, videoCollectionNum, videoTime, videoPriseNum, videoId;
    private String videoSynopsis;

    public VideoBean(int videoId, String videoTag, String videoWriter, String videoCategory, String videoTopic,
                      String videoTitle, String videoContent, int videoPlayNum, int videoDiscussNum, int videoCollectionNum,
                      int videoTime, int videoPriseNum, String videoBcImage, long videoUpTime, String videoSynopsis) {

        this.videoTag = videoTag;
        this.videoWriter = videoWriter;
        this.videoCategory = videoCategory;
        this.videoTopic = videoTopic;
        this.videoTitle = videoTitle;
        this.videoContent = videoContent;
        this.videoBcImage = videoBcImage;
        this.videoPlayNum = videoPlayNum;
        this.videoDiscussNum = videoDiscussNum;
        this.videoCollectionNum = videoCollectionNum;
        this.videoTime = videoTime;
        this.videoPriseNum = videoPriseNum;
        this.videoId = videoId;
        this.videoUpTime = videoUpTime;
        this.videoSynopsis = videoSynopsis;
    }

    public String getVideoSynopsis() {
        return videoSynopsis;
    }

    public void setVideoSynopsis(String videoSynopsis) {
        this.videoSynopsis = videoSynopsis;
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

    public String getVideoBcImage() {
        return videoBcImage;
    }

    public void setVideoBcImage(String videoBcImage) {
        this.videoBcImage = videoBcImage;
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

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public long getVideoUpTime() {
        return videoUpTime;
    }

    public void setVideoUpTime(long videoUpTime) {
        this.videoUpTime = videoUpTime;
    }
}
