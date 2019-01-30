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
    private int id;
    private int writer;
    private String content;
    private long uptime;
    private int parseNum;
    private int discussNum;
    private int shareNum;
    private int uId;
    private String uName;
    private String uHeader;
    private String uIdentity;
    private ArrayList<String> list;

    public MattersFollowBean(int id, int writer, String content, long uptime, int parseNum, int discussNum, int shareNum, int uId, String uName, String uHeader, String uIdentity, ArrayList<String> list) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.uptime = uptime;
        this.parseNum = parseNum;
        this.discussNum = discussNum;
        this.shareNum = shareNum;
        this.uId = uId;
        this.uName = uName;
        this.uHeader = uHeader;
        this.uIdentity = uIdentity;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWriter() {
        return writer;
    }

    public void setWriter(int writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public int getParseNum() {
        return parseNum;
    }

    public void setParseNum(int parseNum) {
        this.parseNum = parseNum;
    }

    public int getDiscussNum() {
        return discussNum;
    }

    public void setDiscussNum(int discussNum) {
        this.discussNum = discussNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

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

    public String getuIdentity() {
        return uIdentity;
    }

    public void setuIdentity(String uIdentity) {
        this.uIdentity = uIdentity;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MattersFollowBean{" +
                "id=" + id +
                ", writer=" + writer +
                ", content='" + content + '\'' +
                ", uptime=" + uptime +
                ", parseNum=" + parseNum +
                ", discussNum=" + discussNum +
                ", shareNum=" + shareNum +
                ", uId=" + uId +
                ", uName='" + uName + '\'' +
                ", uHeader='" + uHeader + '\'' +
                ", uIdentity='" + uIdentity + '\'' +
                ", list=" + list +
                '}';
    }
}

