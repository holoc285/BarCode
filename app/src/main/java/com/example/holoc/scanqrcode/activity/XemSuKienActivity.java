package com.example.holoc.scanqrcode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.SuKienAdapter;
import com.example.holoc.scanqrcode.model.DonVi;
import com.example.holoc.scanqrcode.model.SuKien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class XemSuKienActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<SuKien> msk;
    ArrayList<DonVi> mdv;
    ArrayList<String>mangtendv;
    ArrayList<SuKien> mangtam;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.taosukienoption :
                Intent i1 = new Intent(XemSuKienActivity.this, TaoSuKienActivity.class);
                startActivity(i1);
                return true;
            case R.id.duyettaikhoanoption:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_su_kien,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_su_kien);

        Toolbar tb = (Toolbar) findViewById(R.id.toolBarXemSuKien);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView lv = (ListView) findViewById(R.id.listview);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        msk = new ArrayList<SuKien>();
        mdv = new ArrayList<DonVi>();
        mangtendv = new ArrayList<String>();

        mangtam = new ArrayList<SuKien>();
        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mdv.clear();
                msk.clear();
                mangtendv.clear();
                mangtam.clear();

                for (DataSnapshot item : dataSnapshot.child("donvi").getChildren()) {

                    String tendv = item.child("tendv").getValue(String.class);
                    int madonvi = item.child("madv").getValue(Integer.class);
                    final DonVi donvi = new DonVi(madonvi,tendv);
                    mdv.add(donvi);
                    mangtendv.add(tendv);
                }

                for (DataSnapshot item : dataSnapshot.child("sukien").getChildren()) {
                    String key = item.getKey();
                    String tensk = item.child("tensk").getValue(String.class);
                    int madv = item.child("madv").getValue(Integer.class);
                    String ngay = item.child("ngay").getValue(String.class);
                    String tgbatdau = item.child("tgbatdau").getValue(String.class);
                    String tgketthuc = item.child("tgketthuc").getValue(String.class);
                    String chuthich = item.child("chuthich").getValue(String.class);
                    final SuKien sukien1 = new SuKien(key,tensk,mdv.get(madv-1).getTendv().toString(),ngay, tgbatdau, tgketthuc, chuthich);
                    mangtam.add(sukien1);
                }

                for(int i=mangtam.size()-1; i>=0; i--)
                    msk.add(mangtam.get(i));

                SuKienAdapter arrayAdapter = new SuKienAdapter(XemSuKienActivity.this, R.layout.activity_su_kien_adapter,msk);

                lv.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SUKIEN",databaseError.getMessage());
            }

        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSuKienActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",i);
                bundle.putSerializable("mangdonvi",mangtendv);
                bundle.putSerializable("mang",msk);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }




}
