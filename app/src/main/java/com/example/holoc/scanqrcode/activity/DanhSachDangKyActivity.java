package com.example.holoc.scanqrcode.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.UsersViewHolder;
import com.example.holoc.scanqrcode.model.Users;
import com.example.holoc.scanqrcode.ultil.CheckConnect;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class DanhSachDangKyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mUsersDatabase;
    private ProgressDialog progressDialog;
    private ImageView imageUser;
    private FloatingActionButton btnAddUser;
    private static final int GALLERY_PICK = 1;

    private FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    private StorageReference imageStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_dang_ky);
        Anhxa();
        ActionBar();
        ActionAddUser();



    }


    // Thêm người dùng
    private void ActionAddUser() {
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddUser();
            }
        });

    }

    // DialogAddUser
    private void DialogAddUser() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dùng để goi layout dialog
        dialog.setContentView(R.layout.dialog_user_sua);
        dialog.setCanceledOnTouchOutside(false);

        imageUser = dialog.findViewById(R.id.imge_user_imge_sua);

        final EditText editTen = dialog.findViewById(R.id.edit_user_ten_sua);
        final EditText editMSSV = dialog.findViewById(R.id.edit_user_mssv_sua);
        final EditText editDonVi = dialog.findViewById(R.id.edit_user_donvi_sua);
        final EditText editSDT = dialog.findViewById(R.id.edit_user_sdt_sua);
        Button btnThem = dialog.findViewById(R.id.btn_user_capnhat_sua);
        Button btnHuy = dialog.findViewById(R.id.btn_user_huy_sua);

        btnThem.setText("Thêm");
        btnHuy.setText("Trở về");

        // event upload image
        imageUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SECLECT IMAGE"), GALLERY_PICK);
                return false;
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionProgressbar("Thêm", "Đang thêm người dùng...");

                final String ten = editTen.getText().toString();
                final String mssv = editMSSV.getText().toString();
                final String donvi = editDonVi.getText().toString();
                final String sdt = editSDT.getText().toString();
                final String deviceToken = FirebaseInstanceId.getInstance().getToken();

                // Biểu thức chính qui
                String pattern ="0[0-9]{9,10}";


                if(ten.equals("") | mssv.equals("") | donvi.equals("") | sdt.equals("")){
                    progressDialog.hide();
                    CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Vui lòng điển đầy đủ thông tin");
                }else {
                    if(sdt.matches(pattern)){
//                        CheckConnect.ShowToast(getApplicationContext(),"Bạn đã nhập đúng");
                        // không trùng giữa các hình upload
                        Calendar calendar = Calendar.getInstance();
                        final String KeyImageUser = String.valueOf(calendar.getTimeInMillis());
                        imageStorageRef = imageStorage.getReferenceFromUrl("gs://nckh-1cbe0.appspot.com/Users");
                        StorageReference userImageRef = imageStorageRef.child("images").child(calendar.getTimeInMillis()+".jpg");
                        // final String KeyHinh = String .valueOf(calendar.getTimeInMillis());
//                    imageUser.setScaleType(ImageView.ScaleType.FIT_XY);
                        // upload image
                        // Get the data from an ImageView as bytes
                        imageUser.setDrawingCacheEnabled(true);
                        imageUser.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imageUser.getDrawable()).getBitmap();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        Toast.makeText(DanhSachDangKyActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                        UploadTask uploadTask = userImageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                CheckConnect.ShowToast(getApplicationContext(),"Thêm hình không thành công");
                                progressDialog.dismiss();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                @SuppressWarnings("VisibleForTests")
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                // upload database on database if image upload success
                                // tạo new key
                                String key = mUsersDatabase.push().getKey();

                                Users user = new Users();
                                user.setName(ten);
                                user.setMssv(mssv);
                                user.setImage(downloadUrl.toString());
                                user.setDonvi(donvi);
                                user.setSdt(sdt);
                                user.setQuyen("0");
                                user.setXacnhan("0");
                                user.setDevice_token(deviceToken);

                                mUsersDatabase.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override

                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this,"Thêm thành công");
                                            dialog.dismiss();
                                        } else {
                                            progressDialog.hide();
                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Thêm không thành công");
                                        }
                                    }
                                });

                            }
                        });


                    }else{
                        progressDialog.hide();
                        CheckConnect.ShowToast(getApplicationContext(),"Vui lòng nhập đúng định dạng số điện thoại");
                    }






                }
            }
        });




        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    // start
    @Override
    protected void onStart() {
        super.onStart();

        // lấy dữ liệu từ firebase
        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyler = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.dong_recycler_users,
                UsersViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, final Users model, int position) {
                // đổ  dữ liệu từ firebase về app
                viewHolder.setName(model.getName());
                viewHolder.setUserMSSV(model.getMssv());
                viewHolder.setTrangThai(doiTrangThai(model.getXacnhan()));
                viewHolder.setUserImage(model.getImage(), getApplicationContext());




                // lấy id của user
                final String user_id = getRef(position).getKey();



                // xét sự kiện click vào từng người màn hình chi tiết
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetailUser = new Intent(DanhSachDangKyActivity.this, ChiTietUserActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model",model);
                        bundle.putString("user_id",user_id);
                        intentDetailUser.putExtra("dulieu",bundle);
                        startActivity(intentDetailUser);
                    }
                });

                // button Xóa
                Button btnXoa = viewHolder.mView.findViewById(R.id.dong_user_btnxoa);
                btnXoa.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        mUsersDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mUsersDatabase.child(user_id).removeValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                });
                // button sửa
                final String name= model.getName();
                Button btnSua = viewHolder.mView.findViewById(R.id.dong_user_btnsua);
                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUpdate(user_id, model);
                        //Toast.makeText(getApplicationContext(),model.getName(),Toast.LENGTH_LONG).show();
                    }
                });


            }
        };


        recyclerView.setAdapter(firebaseRecyler);


    }

    // DialogUpdateUser theo idUser và được truyền dữ liệu từ model Users
    private void DialogUpdate(final String idUser, final Users model) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dùng để goi layout dialog
        dialog.setContentView(R.layout.dialog_user_sua);
        dialog.setCanceledOnTouchOutside(false);

        imageUser = dialog.findViewById(R.id.imge_user_imge_sua);

        final EditText editTen = dialog.findViewById(R.id.edit_user_ten_sua);
        final EditText editMSSV = dialog.findViewById(R.id.edit_user_mssv_sua);
        final EditText editDonVi = dialog.findViewById(R.id.edit_user_donvi_sua);
        final EditText editSDT = dialog.findViewById(R.id.edit_user_sdt_sua);
        Button btnCapNhap = dialog.findViewById(R.id.btn_user_capnhat_sua);
        Button btnHuy = dialog.findViewById(R.id.btn_user_huy_sua);

        ///////GET DATA


        //Toast.makeText(getApplicationContext(),model.getName(),Toast.LENGTH_LONG).show();
        editTen.setText(model.getName());
        editMSSV.setText(model.getMssv());
        editDonVi.setText(model.getDonvi());
        editSDT.setText(model.getSdt());
        Picasso.with(getApplicationContext()).load(model.getImage())
                .placeholder(R.drawable.no_image_available)
                .error(R.drawable.err)
                .into(imageUser);


        /////// UPLOAD DATA
//
        imageUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SECLECT IMAGE"), GALLERY_PICK);
                return false;
            }
        });
        btnCapNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionProgressbar("Cập nhât","Đang cập nhật dữ liệu...");

                final String ten = editTen.getText().toString();
                final String mssv = editMSSV.getText().toString();
                final String donvi = editDonVi.getText().toString();
                final String sdt = editSDT.getText().toString();
                final String deviceToken = FirebaseInstanceId.getInstance().getToken();

                // Biểu thức chính qui kiểm tra số điện thoại
                String pattern ="0[0-9]{9,10}";

                if(ten.equals("") | mssv.equals("") | donvi.equals("") | sdt.equals("")){
                    progressDialog.hide();
                    CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Vui lòng điển đầy đủ thông tin cập nhật");
                }else {

                    if(sdt.matches(pattern)){


                        // không trùng giữa các hình upload
                        Calendar calendar = Calendar.getInstance();
                        final String KeyImageUser = String.valueOf(calendar.getTimeInMillis());
                        imageStorageRef = imageStorage.getReferenceFromUrl("gs://nckh-1cbe0.appspot.com/Users");
                        StorageReference userImageRef = imageStorageRef.child("images").child(calendar.getTimeInMillis()+".jpg");
                        // final String KeyHinh = String .valueOf(calendar.getTimeInMillis());
//                    imageUser.setScaleType(ImageView.ScaleType.FIT_XY);
                        // upload image
                        // Get the data from an ImageView as bytes
                        imageUser.setDrawingCacheEnabled(true);
                        imageUser.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imageUser.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = userImageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                CheckConnect.ShowToast(getApplicationContext(),"Cập nhật hình không thành công");
                                progressDialog.dismiss();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                @SuppressWarnings("VisibleForTests")
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                // upload database on database if image upload success
                                Users user = new Users();
                                user.setName(ten);
                                user.setMssv(mssv);
                                user.setImage(downloadUrl.toString());
                                user.setDonvi(donvi);
                                user.setSdt(sdt);
                                user.setQuyen(model.getQuyen());
                                user.setXacnhan(model.getXacnhan());
                                user.setDevice_token(deviceToken);

                                mUsersDatabase.child(idUser).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override

                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Cập nhật thành công");
                                            dialog.dismiss();
                                        } else {
                                            progressDialog.hide();
                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Cập nhật không thành công");
                                        }
                                    }
                                });

                            }
                        });





                    }else{
                        progressDialog.hide();
                        CheckConnect.ShowToast(getApplicationContext(),"Vui lòng nhập đúng định dạng số điện thoại");
                    }



                }






            }
        });
//
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Nhận hình
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri uriImage = data.getData();
            imageUser.setImageURI(uriImage);
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imageUser.setImageBitmap(bitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ProgressBar
    private void ActionProgressbar(String title, String message) {
        progressDialog = new ProgressDialog(DanhSachDangKyActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    // Đổi số ra trạng thái
    private String doiTrangThai(String xacnhan) {
        if (xacnhan.equals("1")) return "Đã xác nhận";
        else return "Chưa xác nhận";
    }

    // ActionBar
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Ánh xạ
    private void Anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_dsdk);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_dsdk);
        btnAddUser =(FloatingActionButton) findViewById(R.id.btn_dangky_add_user);



        // lấy dữ liệu người dùng
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        // đúng kích thước
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
