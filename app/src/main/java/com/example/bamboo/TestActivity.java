package com.example.bamboo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bamboo.adapter.FriendAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.OnAddFriendInterface;
import com.example.bamboo.myview.RecodeButton;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.HuanXinHelper;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TestActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<PersonalBean> list;
    private FriendAdapter adapter;
    public static final int HANDLER_ADD_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
    }

}
