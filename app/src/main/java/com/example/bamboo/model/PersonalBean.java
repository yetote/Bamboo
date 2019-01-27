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
    private String uBg, uHeader;
    private String uName;
    private int uFollow, uFans, uId;
    private String uIdentity;
    //简介
    private String uSynopsis;
    private String uSex, uBirthday;
    private String uTel;

    public PersonalBean(String uBg, String uHeader, String uName, int uFollow, int uFans, int uId, String uIdentity, String uSynopsis, String uSex, String uBirthday, String uTel) {
        this.uBg = uBg;
        this.uHeader = uHeader;
        this.uName = uName;
        this.uFollow = uFollow;
        this.uFans = uFans;
        this.uId = uId;
        this.uIdentity = uIdentity;
        this.uSynopsis = uSynopsis;
        this.uSex = uSex;
        this.uBirthday = uBirthday;
        this.uTel = uTel;
    }

    public String getuTel() {
        return uTel;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
    }

    public String getuBg() {
        return uBg;
    }

    public void setuBg(String uBg) {
        this.uBg = uBg;
    }

    public String getuHeader() {
        return uHeader;
    }

    public void setuHeader(String uHeader) {
        this.uHeader = uHeader;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getuFollow() {
        return uFollow;
    }

    public void setuFollow(int uFollow) {
        this.uFollow = uFollow;
    }

    public int getuFans() {
        return uFans;
    }

    public void setuFans(int uFans) {
        this.uFans = uFans;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuIdentity() {
        return uIdentity;
    }

    public void setuIdentity(String uIdentity) {
        this.uIdentity = uIdentity;
    }

    public String getuSynopsis() {
        return uSynopsis;
    }

    public void setuSynopsis(String uSynopsis) {
        this.uSynopsis = uSynopsis;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public String getuBirthday() {
        return uBirthday;
    }

    public void setuBirthday(String uBirthday) {
        this.uBirthday = uBirthday;
    }
}
