package com.example.bamboo.util;

import com.example.bamboo.myinterface.OnLoginInterface;
import com.example.bamboo.myinterface.OnLoginSuccess;

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
    private static OnLoginSuccess loginSuccess;

    public static void setLoginInterface(OnLoginInterface loginInterface) {
        CallBackUtils.loginInterface = loginInterface;
    }

    public static void setLogin(boolean isLogin, String uName, int error) {
        if (loginInterface != null) {
            loginInterface.login(isLogin, uName, error);
        }
    }

    public static void setLoginSuccess(OnLoginSuccess loginSuccess) {
        CallBackUtils.loginSuccess = loginSuccess;
    }

    public static void setSuccess(String username) {
        if (loginSuccess != null) {
            loginSuccess.success(username);
        }
    }

}
