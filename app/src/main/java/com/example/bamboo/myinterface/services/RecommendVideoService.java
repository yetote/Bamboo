package com.example.bamboo.myinterface.services;

import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.VideoBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RECOMMEND_VIDEO;

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
public interface RecommendVideoService {
    /**
     * 获取推荐视频信息接口
     *
     * @param position 末位索引
     * @return 状态码与视频列表
     */
    @FormUrlEncoded
    @POST(NETWORK_RECOMMEND_VIDEO)
    Observable<JsonBean<VideoBean>> getRecommendVideo(@Field("position") int position);
}
