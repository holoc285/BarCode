package com.example.holoc.scanqrcode.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.ultil.CheckConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;


public class DangKyActivity extends AppCompatActivity {
    private EditText editTextTen, editTextEmail, editTextPass, editTextLaiPass;
    private Button buttonXacnhan, buttonTrove;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        Anhxa();
        ActionToolbar();
        if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
            ActionClick();
        } else {
            CheckConnect.ShowToast(getApplicationContext(), "Vui lòng kiểm tra kết nối");
        }


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

    private void ActionClick() {
        buttonTrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = editTextTen.getText().toString();
                String email = editTextEmail.getText().toString();
                String pass = editTextPass.getText().toString();
                String passlai = editTextLaiPass.getText().toString();

                if (!TextUtils.isEmpty(ten) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    if(pass.equals(passlai)){
                        // tạo progerss
                        mProgressDialog.setTitle("Đăng ký tài khoản");
                        mProgressDialog.setMessage("Vui lòng chờ chờ giây lát để hoàn thành đăng ký");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();

                        DangKy_TaiKhoan(ten, email, pass);

                    }else{
                        CheckConnect.ShowToast(getApplicationContext(), "Mật khẩu không k" +
                                "hớp nhau");
                    }

                } else {
                    CheckConnect.ShowToast(getApplicationContext(), "Vui lòng điền đầy đủ thông tin");
                }

            }
        });
    }

    private void DangKy_TaiKhoan(final String ten, String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // người dùng hiện tại
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    // lấy id của users
                    String uid = current_user.getUid();
                    // lấy dữ liệu từ id của mỗi đối tượng
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    // đẩy dữ liệu lên firebase
                    HashMap<String, String> userMap = new HashMap<String, String>();
                    userMap.put("name", ten);
                    userMap.put("mssv", "default");
                    userMap.put("image", "default");
                    userMap.put("sdt","default");
                    userMap.put("device_token", deviceToken);
                    userMap.put("donvi","default");
                    userMap.put("xacnhan","0");
                    userMap.put("quyen","0");
                    // đẩy lên
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // đẩy lên thành công cho chuyển qua Activity main
                            if (task.isSuccessful()) {
                                mProgressDialog.dismiss();
                                Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                                // giữ màn hình khi thoát ra
                                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                CheckConnect.ShowToast(getApplicationContext(), "Đăng ký tài khoản thành công");
                                finish();
                            }
                        }
                    });

                } else {
                    // ẩn progress
                    mProgressDialog.hide();
                    CheckConnect.ShowToast(getApplicationContext(), "Không thể tạo tài khoản. Vui lòng điền đầy đủ thông tin");
                }
            }
        });
    }

    private void Anhxa() {
        editTextTen = (EditText) findViewById(R.id.edittextDangKyTen);
        editTextEmail = (EditText) findViewById(R.id.edittextDangKyEmail);
        editTextPass = (EditText) findViewById(R.id.edittextDangKyPassWord);
        editTextLaiPass = (EditText) findViewById(R.id.edittextDangKyLaiPassWord);
        buttonXacnhan = (Button) findViewById(R.id.buttonDangky);
        buttonTrove = (Button) findViewById(R.id.buttonHuyDangky);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_app_dangky);
        // khởi tạo FireBase
        mAuth = FirebaseAuth.getInstance();
        // khởi tạo ProgressDialog
        mProgressDialog = new ProgressDialog(this);
    }
}
