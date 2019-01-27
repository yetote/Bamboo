package com.example.bamboo.util;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2019/1/10 14:11
 * @change
 * @chang time
 * @class describe
 */
public class NetworkUtil {
    /**
     * 数据从本地数据库中获取的
     */
    public static final int DATA_FROM_ROOM = 1000;

    /**
     * 请求错误
     */
    public static final int NETWORK_RESULT_ERR = -1;

    /**
     * 请求成功
     */
    public static final int NETWORK_RESULT_OK = 200;

    /**
     * 注册成功
     */
    public static final int NETWORK_REGISTER_OK = 10;

    /**
     * 注册失败
     */
    public static final int NETWORK_REGISTER_ERR = 401;

    /**
     * 用户已注册
     */
    public static final int NETWORK_REGISTER_ERR_USER_REGISTERED = 400;

    /**
     * 用户名重复
     */
    public static final int NETWORK_REGISTER_ERR_USER_USERNAME_EXIST = 101;

    /**
     * 未找到用户
     */
    public static final int NETWORK_LOGIN_ERR_UN_USER = 501;

    /**
     * 密码错误
     */
    public static final int NETWORK_LOGIN_ERR_PWD = 502;


    /**
     * 没有更新的视频
     */
    public static final int NETWORK_VIDEO_ERR_RECOMMEND_UN_UPDATE = 601;
    /**
     * 没有找到视频信息
     */
    public static final int NETWORK_VIDEO_ERR_UN_IM = 602;
    /**
     * 好友已存在
     */
    public static final int NETWORK_CONTACT_ALREADY = 701;
    /**
     * 没有好友申请信息
     */
    public static final int NETWORK_CONTACT_UN_APPLIED = 702;


    public static final String NETWORK_BASE_URL = "http://106.12.221.149:8080/bamboo/bamboo/";
    public static final String NETWORK_RECOMMEND_VIDEO = "video/recommend";
    public static final String NETWORK_REGISTER = "user/userRegister";
    public static final String NETWORK_LOGIN = "user/userLogin";
    public static final String NETWORK_USER_SEARCH = "user/userSearch";
    public static final String NETWORK_USER_ADD_CONTACT = "user/userAddContacts";
    public static final String NETWORK_USER_ADD_SELECT_STATE = "user/userSelectAddIm";
    public static final String NETWORK_USER_ADD_CHANGE_STATE = "user/userChangeAddState";
    public static final String NETWORK_USER_SELECT_IM = "user/userIm";
    public static final String NETWORK_USER_CHANGE_IM = "user/userChangeIm";
    public static final String NETWORK_VIDEO_SELECT_IM = "video/getVideoIm";
}
