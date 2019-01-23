package com.example.bamboo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.entity
 * @class describe
 * @time 2019/1/23 15:04
 * @change
 * @chang time
 * @class describe
 */
@Entity
public class UserEntity {
    @PrimaryKey
    private int uId;
    private String uName;
    private String uHeader;

    private String uBg;

    private String uIdentity;

    private String uSynopsis;

    private String uTel;

    private String uPwd;

    private String uSex;

    private String uBirthday;

    private int uFollow;

    private int uFans;

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
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

    public String getuSynopsis() {
        return uSynopsis;
    }

    public void setuSynopsis(String uSynopsis) {
        this.uSynopsis = uSynopsis;
    }

    public String getuTel() {
        return uTel;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
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
}
