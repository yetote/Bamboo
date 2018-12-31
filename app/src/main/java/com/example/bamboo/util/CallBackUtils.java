package com.example.bamboo.util;

import com.example.bamboo.myinterface.OnAddFriendInterface;
import com.example.bamboo.myinterface.OnLoginInterface;
import com.example.bamboo.myinterface.OnSelectFriendInterface;

import java.util.List;

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
    private static OnSelectFriendInterface friendInterface;
    private static OnAddFriendInterface addFriendInterface;

    public static void setLoginInterface(OnLoginInterface loginInterface) {
        CallBackUtils.loginInterface = loginInterface;
    }

    public static void setLogin(boolean isLogin, String uName, int error) {
        if (loginInterface != null) {
            loginInterface.login(isLogin, uName, error);
        }
    }


    public static void setFriendInterface(OnSelectFriendInterface friendInterface) {
        CallBackUtils.friendInterface = friendInterface;
    }

    public static void setFriendList(List<String> list, int error) {
        if (friendInterface != null) {
            friendInterface.onSelect(list, error);
        }
    }

    public static void setAddFriendInterface(OnAddFriendInterface addFriendInterface) {
        CallBackUtils.addFriendInterface = addFriendInterface;
    }

    public static void setAddSuccess(boolean isSuccess, int error) {
        if (addFriendInterface != null) {
            addFriendInterface.add(isSuccess, error);
        }
    }
}
