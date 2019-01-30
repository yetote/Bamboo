package com.example.bamboo.myinterface.services;

import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.MattersFollowBean;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.util.NetworkUtil;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.services
 * @class describe
 * @time 2019/1/30 14:20
 * @change
 * @chang time
 * @class describe
 */
public interface MattersService {
    /**
     * 添加动态
     *
     * @param uId     作者id
     * @param content 动态内容
     * @return code
     */
    @FormUrlEncoded
    @POST(NetworkUtil.NETWORK_MATTERS_ADD)
    Observable<JsonBean<Integer>> add(@Field("writer") int uId, @Field("content") String content);

    /**
     * 关注的好友动态
     *
     * @param uId userId
     * @return bean
     */
    @FormUrlEncoded
    @POST(NetworkUtil.NETWORK_MATTERS_FOLLOW)
    Observable<JsonBean<MattersFollowBean>> follow(@Field("uId") int uId);
}
