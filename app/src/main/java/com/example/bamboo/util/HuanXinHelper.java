package com.example.bamboo.util;

import android.util.Log;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.myinterface.OnLoginInterface;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Arrays;
import java.util.List;

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

    /**
     * 注册
     *
     * @param uname 用户名
     * @param pwd   密码
     */
    public static void register(String uname, String pwd) {
        REGISTER_STATE = 0;
        new Thread(() -> {
            try {
                EMClient.getInstance().createAccount(uname, pwd);
                MyApplication.getContext().getCallBackUtils().setLogin(true, uname, 0);
            } catch (HyphenateException e) {
                Log.e(TAG, "register: " + e.getErrorCode() + "\n" + e.getDescription());
                MyApplication.getContext().getCallBackUtils().setLogin(false, uname, e.getErrorCode());
            }
        }).start();
    }

    /**
     * 登录
     *
     * @param uname 用户名
     * @param pwd   密码
     */
    public static void login(String uname, String pwd) {
        LOGIN_STATE = 0;
        new Thread(() -> EMClient.getInstance().login(uname, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e(TAG, "登录聊天服务器成功！");
                MyApplication.getContext().getCallBackUtils().setLogin(true, uname, 0);
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e(TAG, "onProgress: ");
            }

            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "登录聊天服务器失败！" + code);
                MyApplication.getContext().getCallBackUtils().setLogin(false, uname, code);
            }
        })).start();
    }

    /**
     * 查询好友列表
     */
    public static void selectFriendList() {
        new Thread(() -> {
            try {
                Log.e(TAG, "run: " + Thread.currentThread().getName());
                List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                MyApplication.getContext().getCallBackUtils().setFriendList(usernames, 0);
            } catch (HyphenateException e) {
                MyApplication.getContext().getCallBackUtils().setFriendList(null, e.getErrorCode());
                Log.e(TAG, "selectFriendList: error" + e.getErrorCode());
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 添加好友
     *
     * @param uName 好友名
     */
    public static void addFriend(String uName, String reason) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(uName, reason);
                    MyApplication.getContext().getCallBackUtils().setAddSuccess(true, 0);
                } catch (HyphenateException e) {
                    MyApplication.getContext().getCallBackUtils().setAddSuccess(false, e.getErrorCode());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
