package com.example.bamboo;

import android.os.Bundle;
import android.view.View;

import com.example.bamboo.myview.RecodeButton;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    private RecodeButton recodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recodeButton = findViewById(R.id.recodeButton);
        recodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recodeButton.setStart(true);
                recodeButton.showAnimation();
            }
        });
    }
}
