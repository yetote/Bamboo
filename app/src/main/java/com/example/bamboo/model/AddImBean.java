package com.example.bamboo.model;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class 好友列表
 * @time 2019/1/15 16:53
 * @change
 * @chang time
 * @class describe
 */
public class AddImBean {
    private int uId, uFollow, uFans, id;
    private String uName, uHeader, uBg, uIdentity, uPwd, username, contactname, state, uSynopsis, uTel, uSex;

    public AddImBean(int uId, String uName, String uHeader, String uBg, String uIdentity, int uFollow, int uFans,
                     String uSynopsis, String uTel, String uPwd, int id, String username, String contactname, String state) {
        this.uId = uId;
        this.uTel = uTel;
        this.uFollow = uFollow;
        this.uFans = uFans;
        this.id = id;
        this.uName = uName;
        this.uHeader = uHeader;
        this.uBg = uBg;
        this.uIdentity = uIdentity;
        this.uPwd = uPwd;
        this.username = username;
        this.contactname = contactname;
        this.state = state;
        this.uSynopsis = uSynopsis;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuTel() {
        return uTel;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuHeader() {
        return uHeader;
    }

    public void setuHeader(String uHeader) {
        this.uHeader = uHeader;
    }

    public String getuBg() {
        return uBg;
    }

    public void setuBg(String uBg) {
        this.uBg = uBg;
    }

    public String getuIdentity() {
        return uIdentity;
    }

    public void setuIdentity(String uIdentity) {
        this.uIdentity = uIdentity;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getuSynopsis() {
        return uSynopsis;
    }

    public void setuSynopsis(String uSynopsis) {
        this.uSynopsis = uSynopsis;
    }
}
