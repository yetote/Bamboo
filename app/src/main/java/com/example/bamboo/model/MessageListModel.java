package com.example.bamboo.model;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class 消息列表model
 * @time 2018/12/25 16:08
 * @change
 * @chang time
 * @class describe
 */
public class MessageListModel {
    private String heads, user, content, identity;
    int userID, msgNum;
    long time;

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public MessageListModel(String heads, String user, String content, String identity, int userID, long time, int msgNum) {
        this.heads = heads;
        this.user = user;
        this.content = content;
        this.identity = identity;
        this.userID = userID;
        this.time = time;
        this.msgNum = msgNum;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getHeads() {
        return heads;
    }

    public void setHeads(String heads) {
        this.heads = heads;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
