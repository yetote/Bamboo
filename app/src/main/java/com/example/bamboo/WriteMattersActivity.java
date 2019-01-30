package com.example.bamboo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bamboo.adapter.WriteMatterAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.myinterface.services.MattersService;
import com.example.bamboo.util.MyGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

public class WriteMattersActivity extends AppCompatActivity {
    public static final int WRITE_MATTERS_IMAGE_CODE = 1;
    public static final int PERMISSION_READ_EXTERNAL_CODE = 2;
    private RecyclerView rv;
    private EditText et;
    private WriteMatterAdapter adappter;
    private ArrayList<String> list;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_matters);

        initView();

        toolbar.inflateMenu(R.menu.write_matters_menu);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.write_matters_menu_sure:
                    if (!TextUtils.isEmpty(et.getText())) {
                        MyApplication.retrofit
                                .create(MattersService.class)
                                .add(MyApplication.uId, et.getText().toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(integerJsonBean -> {
                                    switch (integerJsonBean.getCode()) {
                                        case NETWORK_RESULT_OK:
                                            Toast.makeText(WriteMattersActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                            break;
                                        case NETWORK_RESULT_ERR:
                                            Toast.makeText(WriteMattersActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(WriteMattersActivity.this, "未知错误" + integerJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                });
                    }
                    break;
                default:
                    break;
            }
            return false;
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_CODE);
        } else {
            choosePhoto();
        }
        rv.setLayoutManager(new

                GridLayoutManager(this, 3));
        rv.setAdapter(adappter);
    }

    private void initView() {
        list = new ArrayList<>();
        adappter = new WriteMatterAdapter(list, this);
        rv = findViewById(R.id.write_matters_rv);
        toolbar = findViewById(R.id.write_matters_toolbar);
        et = findViewById(R.id.write_matters_et);
    }

    private void choosePhoto() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.87f)
                .imageEngine(new MyGlideEngine())
                .forResult(WRITE_MATTERS_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WRITE_MATTERS_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    list.addAll(Matisse.obtainPathResult(data));
                    adappter.notifyDataSetChanged();
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    Toast.makeText(this, "您需要开启对应的权限才能正常使用该功能", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
