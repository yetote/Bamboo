package com.example.bamboo.model;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class 直播bean
 * @time 2018/11/29 17:47
 * @change
 * @chang time
 * @class describe
 */
public class RecommendLivingBean {
    private String title, tag, img, user;
    private int spectator;
    private String label;


    public RecommendLivingBean(String title, String tag, String img, String user, String label, int spectator) {
        this.title = title;
        this.tag = tag;
        this.img = img;
        this.user = user;
        this.spectator = spectator;
        this.label=label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getSpectator() {
        return spectator;
    }

    public void setSpectator(int spectator) {
        this.spectator = spectator;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
