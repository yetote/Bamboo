package com.example.bamboo.myinterface;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface
 * @class describe
 * @time 2018/12/26 16:22
 * @change
 * @chang time
 * @class describe
 */
public interface OnLoginInterface {
    /**
     * 登录回调接口
     *
     * @param isLogin 是否登录成功
     * @param uName   用户名
     */
    void login(boolean isLogin, String uName, int error);
}
