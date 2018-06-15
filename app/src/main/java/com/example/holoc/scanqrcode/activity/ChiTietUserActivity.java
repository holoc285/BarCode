package com.example.holoc.scanqrcode.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChiTietUserActivity extends AppCompatActivity {

    private TextView txtName, txtMssv, txtDonvi, txtSDT, txtTrangThai;
    private ImageView imageUser;
    private Button btnXacnhan, btnHuy;

    private DatabaseReference UsersDatabase;
    private FirebaseUser current_user;
    private ProgressDialog progressDialog;

    private String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_user);
        Anhxa();
        GetData();
        XacNhan();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        XacNhan();
//    }

    private void XacNhan() {

        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // xác nhận
                if(doiTrangThaiNguoc(txtTrangThai.getText().toString()).equals("0")){

                    btnXacnhan.setVisibility(View.VISIBLE);
                    btnHuy.setVisibility(View.INVISIBLE);
                    // get dữ liệu của từng người dùng
                    UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UsersDatabase.child("xacnhan").setValue("1");
                            btnXacnhan.setVisibility(View.INVISIBLE);
                            btnHuy.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    btnXacnhan.setVisibility(View.VISIBLE);
                    btnHuy.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doiTrangThaiNguoc(txtTrangThai.getText().toString()).equals("0")){
                    btnXacnhan.setVisibility(View.VISIBLE);
                    btnHuy.setVisibility(View.INVISIBLE);
                }else{
                    btnXacnhan.setVisibility(View.INVISIBLE);
                    btnHuy.setVisibility(View.VISIBLE);
                    UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UsersDatabase.child("xacnhan").setValue("0");
                            btnXacnhan.setVisibility(View.VISIBLE);
                            btnHuy.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }


    private void GetData() {


        ActionProgressbar();

        Bundle bundle = getIntent().getBundleExtra("dulieu");

        if(bundle!=null){
            Users users = (Users) bundle.getSerializable("model");

          //  Toast.makeText(getApplicationContext(),users.getName(),Toast.LENGTH_SHORT).show();
            String name = users.getName();
            String mssv = users.getMssv();
            String image = users.getImage();
            String sdt = users.getSdt();
            String quyen = users.getQuyen();
            String xacnhan = users.getXacnhan();
            String donvi = users.getDonvi();

            // gán vào
            txtName.setText(name);
            txtMssv.setText(mssv);
            txtDonvi.setText(donvi);
            txtSDT.setText(sdt);

            Picasso.with(ChiTietUserActivity.this).load(image)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.err)
                    .into(imageUser);

            progressDialog.dismiss();


        }





        UsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String xacnhan = dataSnapshot.child("xacnhan").getValue().toString();
                txtTrangThai.setText(doiTrangThai(xacnhan));
                if(xacnhan.equals("0")){
                    btnXacnhan.setVisibility(View.VISIBLE);
                    btnHuy.setVisibility(View.INVISIBLE);
                }else{
                    btnXacnhan.setVisibility(View.INVISIBLE);
                    btnHuy.setVisibility(View.VISIBLE);
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String doiTrangThai(String xacnhan){
        if(xacnhan.equals("0"))
            return "Chưa xác nhận";
        else
            return "Đã xác nhận";
    }
    private String doiTrangThaiNguoc(String xacnhan){
        if(xacnhan.equals("Chưa xác nhận")) return "0";
        else return "1";
    }


    private void ActionProgressbar() {
        progressDialog = new ProgressDialog(ChiTietUserActivity.this);
        progressDialog.setTitle("Đang tải dữ liệu người dùng");
        progressDialog.setMessage("Vui lòng chờ khi hệ thống đang cập nhật");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void Anhxa() {
        txtName = (TextView) findViewById(R.id.chitiet_user_textTen);
        txtMssv = (TextView) findViewById(R.id.chitiet_user_textmssv);
        txtDonvi = (TextView) findViewById(R.id.chitiet_user_textdv);
        txtSDT = (TextView) findViewById(R.id.chitiet_user_textsdt);
        txtTrangThai = (TextView) findViewById(R.id.chitiet_user_trangthai);
        imageUser = (ImageView) findViewById(R.id.chitiet_user_image);
        btnXacnhan = (Button) findViewById(R.id.chitiet_user_btnXacNhan);
        btnHuy = (Button) findViewById(R.id.chitiet_user_btnhuy);


        current_user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getBundleExtra("dulieu");
        user_id = bundle.getString("user_id");
        // get dữ liệu của từng người dùng
        UsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


    }
}
