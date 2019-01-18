package com.example.bamboo.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bamboo.R;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.VideoBean;
import com.example.bamboo.myinterface.OnFragmentCallback;
import com.example.bamboo.myinterface.services.RecommendVideoService;
import com.example.bamboo.util.TimeUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.TaskExecutor;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;
import static com.example.bamboo.util.NetworkUtil.NETWORK_VIDEO_ERR_UN_IM;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 推荐视频详情信息
 * @time 2019/1/8 9:47
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoDetailedVideoImFragment extends Fragment implements OnFragmentCallback {
    private Button priseBtn, booingBtn, collectBtn, coinBtn, shareBtn;
    private TextView title, synopsis, playNumTv, discussNumTv, upTimeTv, idTv, priseNumTv, boolNumTv, collectNumTv;
    private static final String TAG = "RecommendVideoDetailedV";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_video_detailed_video_im, null);
        initView(v);

        click();
        return v;
    }

    private void click() {
        priseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationY", 0, -10, 0);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleY", 1, 1.2f, 1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(oa1, oa2);
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setBackground(getResources().getDrawable(R.drawable.prised));
                    }
                });
            }
        });


        booingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationY", 0, 10, 0);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleY", 1, 1.2f, 1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(oa1, oa2);
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setBackground(getResources().getDrawable(R.drawable.booed));
                    }
                });
            }
        });
        coinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "rotationY", 0, 180, 0);
                oa1.setDuration(1000);
                oa1.start();
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
            }
        });

        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "scaleX", 1, 0.8f, 1.2f, 1);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleY", 1, 0.8f, 1.2f, 1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(oa1, oa2);
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setBackground(getResources().getDrawable(R.drawable.collected));
                    }
                });
            }
        });


    }

    private void initView(View v) {
        priseBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_praiseBtn);
        booingBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_booingBtn);
        collectBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_collectBtn);
        coinBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_coinBtn);
        shareBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_shareBtn);
        title = v.findViewById(R.id.fragment_recommend_video_detailed_im_video_title);
        synopsis = v.findViewById(R.id.fragment_recommend_video_detailed_im_synopsis);
        playNumTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_video_play_num);
        discussNumTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_discuss_num);
        upTimeTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_video_up_time);
        idTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_video_id);
        priseNumTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_praiseTv);
        boolNumTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_booingTv);
        collectNumTv = v.findViewById(R.id.fragment_recommend_video_detailed_im_collectTv);
//        changeBtnDrawable(priseBtn, R.drawable.prise);
//        changeBtnDrawable(booingBtn, R.drawable.booing);
//        changeBtnDrawable(collectBtn, R.drawable.collect);
//        changeBtnDrawable(coinBtn, R.drawable.coin);
//        changeBtnDrawable(playNumTv, R.drawable.playnum);
//        changeBtnDrawable(discussNumTv,R.drawable.discuss_num);

    }

    private void changeBtnDrawable(TextView v, int resourceId) {
        Drawable drawable = getActivity().getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, 50, 50);
        v.setCompoundDrawables( drawable,null, null, null);
    }

    @Override
    public void callback(int position) {
        Log.e(TAG, "callback: " + position);
        MyApplication
                .retrofit
                .create(RecommendVideoService.class)
                .getVideoIm(position)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonBean<VideoBean>>() {
                    @Override
                    public void accept(JsonBean<VideoBean> videoBeanJsonBean) throws Exception {
                        switch (videoBeanJsonBean.getCode()) {
                            case NETWORK_RESULT_ERR:
                                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                                break;
                            case NETWORK_RESULT_OK:
                                title.setText(videoBeanJsonBean.getBody().get(0).getVideoTitle());
                                synopsis.setText(videoBeanJsonBean.getBody().get(0).getVideoSynopsis());
                                playNumTv.setText(videoBeanJsonBean.getBody().get(0).getVideoPlayNum() + "");
                                discussNumTv.setText(videoBeanJsonBean.getBody().get(0).getVideoDiscussNum() + "");
                                upTimeTv.setText(TimeUtil.agoTime(videoBeanJsonBean.getBody().get(0).getVideoUpTime()));
                                idTv.setText(videoBeanJsonBean.getBody().get(0).getVideoId() + "");
                                priseNumTv.setText(videoBeanJsonBean.getBody().get(0).getVideoPriseNum() + "");
//                                boolNumTv.setText(videoBeanJsonBean.getBody().get(0).);
                                collectNumTv.setText(videoBeanJsonBean.getBody().get(0).getVideoCollectionNum() + "");
                                break;
                            case NETWORK_VIDEO_ERR_UN_IM:
                                Toast.makeText(getActivity(), "未找到对应的视频信息", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "未知错误" + videoBeanJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }
}
