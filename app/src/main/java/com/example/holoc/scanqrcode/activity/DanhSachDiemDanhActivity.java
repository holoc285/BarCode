package com.example.holoc.scanqrcode.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.DanhSachDiemDanhAdapter;
import com.example.holoc.scanqrcode.model.DanhSachDiemDanh;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class DanhSachDiemDanhActivity extends AppCompatActivity {

    private ListView listDSDiemDanh;
    private DanhSachDiemDanhAdapter adapter;
    private ArrayList<DanhSachDiemDanh> arrayList;
    private Toolbar toolbar;
    private ProgressDialog mDialog;

    private static String mssvDelete;
    private TextView txtDanhSachDDRong;
    //URL Delete sinh viên đã điểm danh
    static String urlUpdate = "https://script.google.com/macros/s/AKfycbxJQfZF1sz11C02O5GAtzbyjF7uBWbUOEpDWqhFGQ2fSvGUv8ur/exec?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_diem_danh);
        mDialog  = new ProgressDialog(DanhSachDiemDanhActivity.this);
        mDialog.setMessage("Vui lòng chờ tải dữ liệu ...");
        mDialog.show();

        txtDanhSachDDRong = (TextView) findViewById(R.id.textViewDanhSachDiemDanhRong);
        listDSDiemDanh      =(ListView) findViewById(R.id.listViewDanhSachDiemDanh);
        arrayList           = new ArrayList<>();
        adapter             = new DanhSachDiemDanhAdapter(this,R.layout.row_list_dsdiemdanh, arrayList);
        getDanhSachSinhVien();
        listDSDiemDanh.setAdapter(adapter);
        toolbar =(Toolbar) findViewById(R.id.toolBarDanhSachDiemDanh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Set default textView hide
        //txtDanhSachDDRong.setVisibility(View.INVISIBLE);

        listDSDiemDanh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //Toast.makeText(MainActivity.this, arrayList.get(i).getMssv(), Toast.LENGTH_SHORT).show();
                mssvDelete = arrayList.get(position).getMssv().toString();
                final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(DanhSachDiemDanhActivity.this);
                dialog.setTitle("Xác nhận");
                dialog.setMessage("Bạn có muốn xóa "+ mssvDelete + " ?");
                dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();
                        new DeleteData().execute();
                        //adapter.notifyDataSetChanged();
                        if (arrayList.isEmpty()){
                            listDSDiemDanh.setVisibility(View.INVISIBLE);

                        }

                    }
                });
                dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        if (arrayList.isEmpty()){
            mDialog.dismiss();
            txtDanhSachDDRong.setVisibility(View.VISIBLE);
        }else {
            txtDanhSachDDRong.setVisibility(View.INVISIBLE);
        }
    }

    public JSONObject deleteData(String id) {
        com.squareup.okhttp.Response response;
        try {
            OkHttpClient client = new OkHttpClient();

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(urlUpdate+"action=delete&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());

        } catch (@NonNull IOException | JSONException e) {

        }
        return null;
    }

    class DeleteData extends AsyncTask<Void, Void, Void> {

        String result;
        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = deleteData(mssvDelete);

            try {
                if (jsonObject != null) {
                    result=jsonObject.getString("result");
                }
            } catch (JSONException je) {

            }
            return null;

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }
    }

    public void getDanhSachSinhVien() {
        RequestQueue requestQueue = Volley.newRequestQueue(DanhSachDiemDanhActivity.this);
        String url = "https://script.google.com/macros/s/AKfycbw-HzfM9sUNvlFpzJW-T_C1SUT7xIy-hJ-jpS7OsbeKUeKvDz8/exec";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                if (response.isEmpty()){
                    mDialog.dismiss();
                    Toast.makeText(DanhSachDiemDanhActivity.this, "Danh sách điểm danh rỗng !!!", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        JSONObject jsonOb = new JSONObject(response);
                        JSONObject json = jsonOb.getJSONObject("danhSachCoMat");

                        JSONArray jsonArrayUser = json.getJSONArray("user");
                        for (int i = 0; i < jsonArrayUser.length(); i++) {
                            JSONObject object = jsonArrayUser.getJSONObject(i);
                            String mssv = object.getString("mssv");
                            String time = object.getString("gioDiemDanh");
                            String ghiChu = object.getString("ghiChu");
                            arrayList.add(new DanhSachDiemDanh(mssv, time, ghiChu));
                            adapter.notifyDataSetChanged();
                        }
                        mDialog.dismiss();
//                    JSONObject j = new JSONObject("Hello:jhdsjd");
//                    Toast.makeText(DanhSachDiemDanhActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DanhSachDiemDanhActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);

    }
}
