package com.example.holoc.scanqrcode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.holoc.scanqrcode.R;

public class StartActivity extends AppCompatActivity {
    private Button buttonDangKy, buttonDangnhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        AnhXa();
        ActionClick();
    }

    private void ActionClick() {
        buttonDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AnhXa() {
        buttonDangKy = (Button) findViewById(R.id.buttonChuaCoTaiKhoan);
        buttonDangnhap = (Button) findViewById(R.id.buttonDaCoTaiKhoan);
    }
}
