package com.example.bamboo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.pm.PackageInfoCompat;
import retrofit2.http.HEAD;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.util.MyGlideEngine;
import com.example.bamboo.util.StatusBarUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.ByteArrayOutputStream;

public class ChangeImActivity extends AppCompatActivity {
    private RelativeLayout headRl, nicknameRl, uidRl, cardRl, sexRl, birthdayRl, synopsisRl;
    private ImageView headIv;
    private TextView nicknameTv, uidTv, cardTv, sexTv, birthdayTv, synopsisTv;
    public static final int PERMISSION_READ_STORAGE = 1;
    public static final int HEADER_IMAGE_CODE = 2;
    public static final int HEADER_CROP_CODE = 3;
    private static int output_X = 600;
    private static int output_Y = 600;
    private static final String TAG = "ChangeImActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.changedStatusBar(this);
        setContentView(R.layout.activity_change_im);
        initView();
        click();
    }

    private void click() {
        View view = LayoutInflater.from(ChangeImActivity.this).inflate(R.layout.dialog_person_change_im_head, null);
        TextView camera = view.findViewById(R.id.dialog_change_im_camera);
        TextView choose = view.findViewById(R.id.dialog_change_im_choose);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeImActivity.this, "点击了camera", Toast.LENGTH_SHORT).show();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ChangeImActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChangeImActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_STORAGE);
                } else {
                    chooseHeader();
                }
            }
        });
        headRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager.LayoutParams lp = ChangeImActivity.this.getWindow()
                        .getAttributes();
                lp.alpha = 0.4f;
                ChangeImActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                ChangeImActivity.this.getWindow().setAttributes(lp);

                PopupWindow popupWindow = new PopupWindow(view);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.setFocusable(true);
                popupWindow.setAnimationStyle(R.style.popwin_anim_style);
                popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                popupWindow.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = ChangeImActivity.this.getWindow().getAttributes();
                    lp1.alpha = 1f;
                    ChangeImActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    ChangeImActivity.this.getWindow().setAttributes(lp1);
                });
            }
        });

        nicknameRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        uidRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cardRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sexRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        birthdayRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        synopsisRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void chooseHeader() {
        Matisse.from(ChangeImActivity.this)
                .choose(MimeType.ofImage())
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.87f)
                .imageEngine(new MyGlideEngine())
                .forResult(HEADER_IMAGE_CODE);
    }

    private void initView() {
        headRl = findViewById(R.id.change_im_head_ll);
        nicknameRl = findViewById(R.id.change_im_nick_name_ll);
        uidRl = findViewById(R.id.change_im_id_ll);
        cardRl = findViewById(R.id.change_im_card_ll);
        sexRl = findViewById(R.id.change_im_sex_ll);
        birthdayRl = findViewById(R.id.change_im_birthday_ll);
        synopsisRl = findViewById(R.id.change_im_synopsis_ll);

        headIv = findViewById(R.id.change_im_head_iv);

        nicknameTv = findViewById(R.id.change_im_nick_name_tv);
        uidTv = findViewById(R.id.change_im_id_tv);
        cardTv = findViewById(R.id.change_im_card_tv);
        sexTv = findViewById(R.id.change_im_sex_tv);
        birthdayTv = findViewById(R.id.change_im_birthday_tv);
        synopsisTv = findViewById(R.id.change_im_synopsis_tv);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseHeader();
                } else {
                    Toast.makeText(this, "请允许读取手机文件的权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HEADER_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
//                    Glide.with(ChangeImActivity.this)
//                            .load(Matisse.obtainPathResult(data).get(0))
//                            .into(headIv);
                    cropPhoto(Uri.parse(Matisse.obtainPathResult(data).get(0)));
                }
                break;
            case HEADER_CROP_CODE:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();

                    Glide.with(this).load(bytes).into(headIv);
                }

                break;
        }
    }

    private void cropPhoto(Uri uri) {
        Log.e(TAG, "cropPhoto: " + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //把裁剪的数据填入里面

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, HEADER_CROP_CODE);
    }
}
