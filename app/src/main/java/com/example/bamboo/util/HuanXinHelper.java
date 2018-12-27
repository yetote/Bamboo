package com.example.bamboo.util;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2018/12/26 13:20
 * @change
 * @chang time
 * @class describe
 */
public class HuanXinHelper {
    private static final String TAG = "HuanXinHelper";
    public static int REGISTER_STATE, LOGIN_STATE;

    public static int register(String uname, String pwd) {
        REGISTER_STATE = 0;
        new Thread(() -> {
            try {
                EMClient.getInstance().createAccount(uname, pwd);
            } catch (HyphenateException e) {
                Log.e(TAG, "register: " + e.getErrorCode() + "\n" + e.getDescription());
                REGISTER_STATE = e.getErrorCode();
            }
        }).start();
        return REGISTER_STATE;
    }

    public static int login(String uname, String pwd) {
        LOGIN_STATE = 0;
        new Thread(() -> EMClient.getInstance().login("1234567901", "1", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e(TAG, "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e(TAG, "onProgress: ");
            }

            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "登录聊天服务器失败！" + code);
                LOGIN_STATE = code;
            }
        })).start();
        return LOGIN_STATE;
    }
}
