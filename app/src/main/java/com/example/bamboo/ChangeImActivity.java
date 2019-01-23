package com.example.bamboo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.services.UserService;
import com.example.bamboo.util.MyGlideEngine;
import com.example.bamboo.util.StatusBarUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

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
        MyApplication.retrofit.create(UserService.class).userIm(MyApplication.uId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<JsonBean<PersonalBean>>() {
            @Override
            public void accept(JsonBean<PersonalBean> personalBeanJsonBean) throws Exception {
                if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                    nicknameTv.setText(personalBeanJsonBean.getBody().get(0).getuName());
                    sexTv.setText(personalBeanJsonBean.getBody().get(0).getuSex());
                    birthdayTv.setText(personalBeanJsonBean.getBody().get(0).getuBirthday());
                    synopsisTv.setText(personalBeanJsonBean.getBody().get(0).getuSynopsis());
                }
            }
        });
        uidTv.setText(MyApplication.uId + "");
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

        nicknameRl.setOnClickListener(v -> {
            View nicknameView = LayoutInflater.from(ChangeImActivity.this).inflate(R.layout.dialog_person_change_im_nickname, null);
            EditText nicknameEt = nicknameView.findViewById(R.id.dialog_change_im_nickname);
            new AlertDialog.Builder(ChangeImActivity.this)
                    .setView(nicknameView)
                    .setTitle("修改昵称")
                    .setNegativeButton("取消", (dialog, which) -> {

                    })
                    .setPositiveButton("确定", (dialog, which) -> {
                        MyApplication.retrofit.create(UserService.class)
                                .changeUserIm(MyApplication.uId, nicknameEt.getText().toString(), "nickname")
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe(personalBeanJsonBean -> {
                                    if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                                        Toast.makeText(ChangeImActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        nicknameTv.setText(nicknameEt.getText());
                                    } else {
                                        Toast.makeText(ChangeImActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .create()
                    .show();
        });

        uidRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeImActivity.this, "uid:" + uidTv.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        cardRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeImActivity.this, "该功能暂未开放:", Toast.LENGTH_SHORT).show();

            }
        });

        sexRl.setOnClickListener(v -> {
            View sexView = LayoutInflater.from(ChangeImActivity.this).inflate(R.layout.dialog_person_change_im_sex, null);
            RadioButton boy = sexView.findViewById(R.id.dialog_change_im_sex_boy_rb);
            RadioButton girl = sexView.findViewById(R.id.dialog_change_im_sex_girl_rb);
            AlertDialog alertDialog = new AlertDialog.Builder(ChangeImActivity.this)
                    .setView(sexView)
                    .setTitle("修改性别")
                    .create();
            alertDialog.show();
            boy.setOnClickListener(v12 -> MyApplication.retrofit.create(UserService.class)
                    .changeUserIm(MyApplication.uId, "男", "sex")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(personalBeanJsonBean -> {
                        if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                            Toast.makeText(ChangeImActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            sexTv.setText("男");
                        } else {
                            Toast.makeText(ChangeImActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }));
            girl.setOnClickListener(v1 -> MyApplication.retrofit.create(UserService.class)
                    .changeUserIm(MyApplication.uId, "女", "sex")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(personalBeanJsonBean -> {
                        if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                            Toast.makeText(ChangeImActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            sexTv.setText("女");
                        } else {
                            Toast.makeText(ChangeImActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }));
        });

        birthdayRl.setOnClickListener(v ->

        {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(ChangeImActivity.this, (view1, year1, month1, dayOfMonth) -> {
                String s = String.format("%d-%d-%d", year1, month1 + 1, dayOfMonth);
                MyApplication.retrofit.create(UserService.class)
                        .changeUserIm(MyApplication.uId, s, "birthday")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(personalBeanJsonBean -> {
                            if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                                Toast.makeText(ChangeImActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                birthdayTv.setText(s);
                            } else {
                                Toast.makeText(ChangeImActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }, year, month, day);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.show();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("name",  nicknameTv.getText().toString());
        i.putExtra("sex",  sexTv.getText().toString());
        ChangeImActivity.this.setResult(RESULT_OK, i);
        super.onBackPressed();
    }
}
