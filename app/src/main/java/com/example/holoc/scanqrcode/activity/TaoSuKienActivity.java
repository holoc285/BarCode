package com.example.holoc.scanqrcode.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.SuKien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaoSuKienActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> mangtendv = new ArrayList<String>();
    int madv;
    EditText ngay;
    SuKien sukien;
    EditText tgbatdau;
    EditText tgketthuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_su_kien);

        Toolbar tb = (Toolbar)findViewById(R.id.toolBarTaoSuKien);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        final EditText tensk = (EditText) findViewById(R.id.tensk);
        ngay = (EditText) findViewById(R.id.ngay);
        tgbatdau = (EditText) findViewById(R.id.tgbatdau);
        tgketthuc = (EditText) findViewById(R.id.tgketthuc);
        final EditText chuthich = (EditText) findViewById(R.id.chuthich);
        final Spinner donvi = (Spinner)findViewById(R.id.donvi);
        Button tao = (Button)findViewById(R.id.tao);

        ngay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonNgay();
            }
        });

        tgbatdau.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonThoiGianBatDau();
            }
        });

        tgketthuc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonThoiGianKetThuc();
            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        myRef.child("donvi").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    String ten = item.child("tendv").getValue(String.class);
                    mangtendv.add(ten.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mangtendv.add("Chọn đơn vị---");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        mangtendv


                );

        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        donvi.setAdapter(adapter);

        donvi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                        View arg1,
                                        int arg2,
                                        long arg3) {

                madv = arg2;

            }
            //Nếu không chọn gì cả
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                madv = 0;

            }


        });


        tao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {

                sukien = new SuKien(tensk.getText().toString(),madv,ngay.getText().toString(),tgbatdau.getText().toString(),
                        tgketthuc.getText().toString(),chuthich.getText().toString());
                taosukien(sukien);
            }
        });

    }



    public void taosukien(SuKien sukien){
        myRef.child("sukien").push()
                .setValue(sukien)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           Toast.makeText(TaoSuKienActivity.this,"Tạo thành công",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(TaoSuKienActivity.this, XemSuKienActivity.class);
                            startActivity(i);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // callback.onGetDataFailed(e.getMessage());
                        Toast.makeText(TaoSuKienActivity.this,"Thất bại",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TaoSuKienActivity.this, XemSuKienActivity.class);
                        startActivity(i);
                    }
                });
    }

    public void chonNgay(){
        final Calendar calendar= Calendar.getInstance();
        int gngay = calendar.get(Calendar.DATE);
        int gthang = calendar.get(Calendar.MONTH);
        int gnam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TaoSuKienActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ngay.setText(simpleDateFormat.format(calendar.getTime()));

            }
        },gnam,gthang,gngay);
        datePickerDialog.show();

    }

    public void chonThoiGianBatDau(){
        final Calendar calendar= Calendar.getInstance();
        final int gio = calendar.get(Calendar.HOUR_OF_DAY);
        final int phut = calendar.get(Calendar.MINUTE);
        // int giay = calendar.get(Calendar.SECOND);


        TimePickerDialog timePickerDialog = new TimePickerDialog(TaoSuKienActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
               // calendar.set(i,i1);
              //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                //calendar.set(i,i1);
                if(i<10) {
                    if (i1 < 10)
                        tgbatdau.setText("0" + i + ":0" + i1);
                    else
                        tgbatdau.setText("0"+i +":"+ i1);

                }
                else {
                    if (i1 < 10)
                        tgbatdau.setText(i + ":0" + i1);
                    else
                        tgbatdau.setText(i + ":"+i1);
                }

            }
        },gio,phut,false);
        timePickerDialog.show();

    }

    public void chonThoiGianKetThuc(){
        final Calendar calendar= Calendar.getInstance();
        final int gio = calendar.get(Calendar.HOUR_OF_DAY);
        final int phut = calendar.get(Calendar.MINUTE);
        // int giay = calendar.get(Calendar.SECOND);


        TimePickerDialog timePickerDialog = new TimePickerDialog(TaoSuKienActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if(i<10) {
                    if (i1 < 10)
                        tgketthuc.setText("0" + i + ":0" + i1);
                    else
                        tgketthuc.setText("0"+i +":"+ i1);

                }
                else {
                    if (i1 < 10)
                        tgketthuc.setText(i + ":0" + i1);
                    else
                        tgketthuc.setText(i + ":"+i1);
                }

            }
        },gio,phut,false);
        timePickerDialog.show();

    }
}
