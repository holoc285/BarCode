package com.example.holoc.scanqrcode.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.ultil.CheckConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DangNhapActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPass;
    private Button buttonDangnhap, buttonTrove;
    private Toolbar mToolbar;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AnhXa();
        ActionToolbar();
        if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
            ActionClick();
        } else {
            CheckConnect.ShowToast(getApplicationContext(), "Vui lòng kiểm tra kết nối");
        }

    }

    private void ActionClick() {
        buttonTrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String pass = editTextPass.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    mProgress.setTitle("Đăng nhập tài khoản");
                    mProgress.setMessage("Vui lòng chờ giây lát để hoàn thành đăng nhập");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    Dangnhap_Taikhoan(email, pass);
                } else {
                    CheckConnect.ShowToast(getApplicationContext(), "Vui lòng điền đầy đủ thông tin đăng nhập");
                }
            }
        });
    }

    private void Dangnhap_Taikhoan(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                    // id user
                    String current_user_id = mAuth.getCurrentUser().getUid();
                    // mã thiết bị
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
//                    CheckConnect.ShowToast(getApplicationContext(), "Đăng nhập tài khoản thành công");
                    finish();
                    // tạo dữ liệu cho device
//                    mUserDatabase.child(current_user_id)
//                            .child("device_token")
//                            .setValue(deviceToken)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                }
//                            });


                } else {
                    // ẩn progress
                    mProgress.hide();
                    Log.w("signInWithEmail:failed", task.getException());
                    CheckConnect.ShowToast(getApplicationContext(), "Không thể đăng nhập tài khoản. Vui lòng kiểm tra tài khoản");
                }
            }
        });
    }

    private void ActionToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        editTextEmail = (EditText) findViewById(R.id.edittextDangnhapEmail);
        editTextPass = (EditText) findViewById(R.id.edittextDangnhapPassWord);
        buttonDangnhap = (Button) findViewById(R.id.buttonDangnhap);
        buttonTrove = (Button) findViewById(R.id.buttonHuyDangnhap);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_app_dangnhap);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(DangNhapActivity.this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}
