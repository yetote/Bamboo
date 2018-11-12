package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bamboo.util.NineImageView;
import com.example.bamboo.util.PlayerView;
import com.example.bamboo.util.TextRecourseReader;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button btn;
    PlayerView playerView;
    int w, h;
    String path;
    NineImageView nineImageView;
    private ArrayList<String> list;
    private ImageView iv1, iv4, iv3, iv2;
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        try {
            nineImageView.setUrlList(list);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Log.e(TAG, "onCreate: " + iv1.getWidth());

    }

    private void initView() {
        nineImageView = findViewById(R.id.nineIv);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        list = new ArrayList<>();
        list.add("https://pic4.zhimg.com/v2-cd32ba2a1af564ccd3d0414c7493a35a_1200x500.jpg");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTE0vNmnOb5QZhPKOgSBPw6GO_xZbZ-jTZ4r9NW3MODLwC9-Tw1xg");
        list.add("data:image/jpeg;base64,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");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeWyiTTcdKqXCqNsFxq41z8TGTJZBUgBtCEG8eABEckGe23e4_uQ");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTBvtvr1TE0K1uyxYPbM3EyGhfzP1BfAauaoTODmWyM9hPl_p6");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRim5uDa-3ZIYeDPxyvvzGB_UL2ac_AWVCkwIDa2fmNbTPcu-ii");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeSiaYk4ZGCfsCq7l11cmv-cGGU6a_eEwPhZrPYQkPsnrnqLze");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqV2uVbjkdtYPenl_Y0l9mm68hVIspc02kn4FCOcgo8y2ukAtFTg");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVesfK7q3JxZsmJwxvtQZonVKWOkvnWs4tSeBPaT2KoBOh5KXmWw");

        Log.e(TAG, "initView: ");
    }
}
