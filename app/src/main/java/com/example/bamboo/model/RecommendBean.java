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
public class RecommendBean {
    String url;
    String tag;
    String writer;
    String category;
    String topic;
    String title;
    int playNum, discussNum, collectionNum, videoTime;

    public RecommendBean(String url, String tag, String writer, String category, String topic, String title, int playNum, int discussNum, int collectionNum, int videoTime) {
        this.url = url;
        this.tag = tag;
        this.writer = writer;
        this.category = category;
        this.topic = topic;
        this.title = title;
        this.playNum = playNum;
        this.discussNum = discussNum;
        this.collectionNum = collectionNum;
        this.videoTime = videoTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public int getDiscussNum() {
        return discussNum;
    }

    public void setDiscussNum(int discussNum) {
        this.discussNum = discussNum;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    @Override
    public String toString() {
        return "RecommendBean{" +
                "url='" + url + '\'' +
                ", tag='" + tag + '\'' +
                ", writer='" + writer + '\'' +
                ", category='" + category + '\'' +
                ", topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", playNum=" + playNum +
                ", discussNum=" + discussNum +
                ", collectionNum=" + collectionNum +
                ", videoTime=" + videoTime +
                '}';
    }
}
