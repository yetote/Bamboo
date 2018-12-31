package com.example.bamboo.myinterface;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface
 * @class 添加好友接口
 * @time 2018/12/31 18:16
 * @change
 * @chang time
 * @class describe
 */
public interface OnAddFriendInterface {
    /**
     * 添加好友接口回调
     *
     * @param isSuccess 是否添加成功
     * @param code      错误码
     */
    void add(boolean isSuccess, int code);
}
