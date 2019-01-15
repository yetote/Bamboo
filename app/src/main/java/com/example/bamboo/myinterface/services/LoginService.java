package com.example.bamboo.myinterface.services;

import com.example.bamboo.model.JsonBean;
import com.example.bamboo.util.NetworkUtil;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.services
 * @class 登录
 * @time 2019/1/14 11:21
 * @change
 * @chang time
 * @class describe
 */
public interface LoginService {
    @FormUrlEncoded
    @POST(NetworkUtil.NETWORK_LOGIN)
    Observable<JsonBean<Integer>> login(@Field("uTel") String tel, @Field("uPwd") String pwd);

}
