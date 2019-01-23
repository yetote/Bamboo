package com.example.bamboo.myinterface.services;

import com.example.bamboo.model.AddImBean;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.util.NetworkUtil;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.example.bamboo.util.NetworkUtil.NETWORK_REGISTER;
import static com.example.bamboo.util.NetworkUtil.NETWORK_USER_ADD_CHANGE_STATE;
import static com.example.bamboo.util.NetworkUtil.NETWORK_USER_ADD_CONTACT;
import static com.example.bamboo.util.NetworkUtil.NETWORK_USER_ADD_SELECT_STATE;
import static com.example.bamboo.util.NetworkUtil.NETWORK_USER_CHANGE_IM;
import static com.example.bamboo.util.NetworkUtil.NETWORK_USER_SEARCH;
import static com.example.bamboo.util.NetworkUtil.NETWORK_USER_SELECT_IM;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.services
 * @class describe
 * @time 2019/1/15 13:27
 * @change
 * @chang time
 * @class describe
 */
public interface UserService {
    /**
     * 登录
     *
     * @param tel 用户名
     * @param pwd 密码
     * @return code
     */
    @FormUrlEncoded
    @POST(NetworkUtil.NETWORK_LOGIN)
    Observable<JsonBean<Integer>> login(@Field("uTel") String tel, @Field("uPwd") String pwd);

    /**
     * 注册
     *
     * @param tel 用户手机号码
     * @param pwd 密码
     * @return result code
     */
    @FormUrlEncoded
    @POST(NETWORK_REGISTER)
    Observable<JsonBean<Integer>> register(@Field("uTel") String tel,
                                           @Field("uPwd") String pwd);

    /**
     * 用户搜索(用于添加好友)
     *
     * @param tel 用户手机号码
     * @return
     */
    @FormUrlEncoded
    @POST(NETWORK_USER_SEARCH)
    Observable<JsonBean<PersonalBean>> search(@Field("uTel") String tel);

    /**
     * 添加好友
     *
     * @param username    用户名
     * @param id          id
     * @param state       添加状态
     * @param contactName 申请好友名
     * @return code
     */
    @FormUrlEncoded
    @POST(NETWORK_USER_ADD_CONTACT)
    Observable<JsonBean<Integer>> addContact(@Field("uTel") String username,
                                             @Field("id") int id,
                                             @Field("state") String state,
                                             @Field("cTel") String contactName);

    /**
     * 查询好友添加状态
     *
     * @param username 当前用户
     * @param state    好友状态
     * @return code
     */
    @FormUrlEncoded
    @POST(NETWORK_USER_ADD_SELECT_STATE)
    Observable<JsonBean<AddImBean>> selectAddContactIm(@Field("uTel") String username, @Field("state") String state);


    /**
     * 改变好友状态(用于同意好友申请)
     *
     * @param id      id
     * @param state   状态
     * @param user    用户
     * @param contact 好友
     * @return code
     */
    @FormUrlEncoded
    @POST(NETWORK_USER_ADD_CHANGE_STATE)
    Observable<JsonBean<Integer>> changeAddState(@Field("id") int id, @Field("state") String state,
                                                 @Field("user") String user, @Field("contact") String contact);

    /**
     * 查询用户信息
     *
     * @param id 用户id
     * @return personalBean
     */
    @FormUrlEncoded
    @POST(NETWORK_USER_SELECT_IM)
    Observable<JsonBean<PersonalBean>> userIm(@Field("uId") int id);

    /**
     * 修改用户信息
     *
     * @param user 用户id
     * @param im   修改的信息
     * @param row  需要修改的用户索引
     * @return PersonalBean
     */
    @FormUrlEncoded
    @POST(NETWORK_USER_CHANGE_IM)
    Observable<JsonBean<PersonalBean>> changeUserIm(@Field("uId") int user, @Field("im") String im, @Field("row") String row);
}
