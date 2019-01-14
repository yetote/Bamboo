package com.example.bamboo.myinterface.services;

import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.VideoBean;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RECOMMEND_VIDEO;
import static com.example.bamboo.util.NetworkUtil.NETWORK_REGISTER;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.services
 * @class describe
 * @time 2019/1/10 14:14
 * @change
 * @chang time
 * @class describe
 */
public interface RegisterService {

    @FormUrlEncoded
    @POST(NETWORK_REGISTER)
    Observable<JsonBean<VideoBean>> register(@Field("uTel") String tel,
                                             @Field("uPwd") String pwd);
}
