package com.example.bamboo.model;

import java.util.ArrayList;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class describe
 * @time 2018/11/12 14:43
 * @change
 * @chang time
 * @class describe
 */
public class MattersFollowBean {
    private ArrayList<String> imageList;
    private String headImg;
    private String identity;
    private String name;
    private String content;
    private long releaseTime;

    @Override
    public String toString() {
        return "MattersFollowBean{" +
                "imageList=" + imageList +
                ", headImg='" + headImg + '\'' +
                ", identity='" + identity + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", releaseTime=" + releaseTime +
                '}';
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * @param imageList   图片列表
     * @param headImg     头像
     * @param identity    身份
     * @param name        用户名
     * @param content     内容
     * @param releaseTime 发布时间
     */
    public MattersFollowBean(ArrayList<String> imageList, String headImg, String identity, String name, String content, long releaseTime) {
        this.imageList = imageList;
        this.headImg = headImg;
        this.identity = identity;
        this.name = name;
        this.content = content;
        this.releaseTime = releaseTime;
    }
}
