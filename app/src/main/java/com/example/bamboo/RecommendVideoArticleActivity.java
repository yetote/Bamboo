package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.bamboo.util.StatusBarUtils;

import static android.view.View.SCROLLBARS_OUTSIDE_OVERLAY;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo
 * @class 推荐文章详情
 * @time 2018/11/28 15:41
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoArticleActivity extends AppCompatActivity {
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.changedStatusBar(this);
        setContentView(R.layout.activity_recommend_video_article);
        initView();

        Intent i = getIntent();
        if (i != null) {
            String content = i.getStringExtra("video_content");
            //取消滚动条
            wv.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
            //不支持缩放功能
            wv.getSettings().setSupportZoom(false);
            wv.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        }
    }

    private void initView() {
        wv = findViewById(R.id.recommend_video_article_wv);
    }
}

