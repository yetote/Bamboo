package com.example.bamboo.model;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class 个人信息bean
 * @time 2018/12/5 15:33
 * @change
 * @chang time
 * @class describe
 */
public class PersonalBean {
    private String bg, headImg;
    private String uName;
    private int followNum, fansNum, uid;
    private String identity;
    //简介
    private String synopsis;

    public PersonalBean(String bg, String headImg, String uName, int uid, int followNum, int fansNum, String identity, String synopsis) {
        this.bg = bg;
        this.headImg = headImg;
        this.uName = uName;
        this.uid = uid;
        this.followNum = followNum;
        this.fansNum = fansNum;
        this.identity = identity;
        this.synopsis = synopsis;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return uName;
    }

    public void setName(String uName) {
        this.uName = uName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
