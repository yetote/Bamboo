package com.example.bamboo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.model
 * @class describe
 * @time 2019/1/10 14:15
 * @change
 * @chang time
 * @class describe
 */
public class JsonBean<T> {
    private int code;
    private ArrayList<T> body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<T> getBody() {
        return body;
    }

    public void setBody(ArrayList<T> list) {
        this.body = list;
    }
}
