package com.example.bamboo.util;

import com.example.bamboo.myinterface.OnLoginInterface;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 回调接口管理类
 * @time 2018/12/26 16:25
 * @change
 * @chang time
 * @class describe
 */
public class CallBackUtils {
    private static OnLoginInterface loginInterface;

    public static void setLoginInterface(OnLoginInterface loginInterface) {
        CallBackUtils.loginInterface = loginInterface;
    }

    public static void setLogin(boolean isLogin, String uName) {
        if (loginInterface != null) {
            loginInterface.login(isLogin, uName);
        }
    }
}
