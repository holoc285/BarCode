package com.example.holoc.scanqrcode.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.MenuAdapter;
import com.example.holoc.scanqrcode.model.Menu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {

    private String scannedData;
    private Button scanBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ArrayList<Menu> arrayMenu;
    private ArrayList<Menu> arrayCauHinh;
    private MenuAdapter adapterMenu;
    private MenuAdapter adapterCauHinh;
    private ListView listViewMenu, listViewCauHinh;
    private ImageButton imageViewCauHinh;
    private ImageButton imageViewMenu;
    private boolean batListView = false;
    private static String mssv, ghiChu;
    private final Activity activity = this;
    private FabSpeedDial fabSpeedDial;
    private static String mssvDelete;
    DatabaseReference databaseReference;
    //URL Delete sinh viên đã điểm danh
    static String urlUpdate = "https://script.google.com/macros/s/AKfycbxJQfZF1sz11C02O5GAtzbyjF7uBWbUOEpDWqhFGQ2fSvGUv8ur/exec?";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        // Tạo action bar
        Actionbar();
        CatchChonItemListViewMenu();
        ActionClickIconListView();
        ActionClickIconAgainView();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("User").setValue("Test");

        //new UpdateDataActivity().execute();

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.actionDiemDanhBoSung){
                    DialogDiemDanhSinhVien();
                    //Toast.makeText(MainActivity.this, ""+1, Toast.LENGTH_SHORT).show();
                }
                if (menuItem.getItemId()==R.id.actionDanhSachDiemDanh){
                    //Toast.makeText(MainActivity.this, ""+2, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, DanhSachDiemDanhActivity.class));
                }
                return true;
            }
        });
         /*
        Bắt sự kiện điểm danh sinh viên thủ công (Thêm sinh viên) vào trang tính
         */

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Đặt QR Code hoặc Barcode cần quét theo đường chỉ ngang");
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    private void ActionClickIconAgainView() {
        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewCauHinh.setVisibility(View.INVISIBLE);
                listViewMenu.setVisibility(View.VISIBLE);
                imageViewMenu.setVisibility(View.INVISIBLE);
                imageViewCauHinh.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ActionClickIconListView() {
        imageViewCauHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewCauHinh.setVisibility(View.VISIBLE);
                listViewMenu.setVisibility(View.INVISIBLE);
                imageViewMenu.setVisibility(View.VISIBLE);
                imageViewCauHinh.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void CatchChonItemListViewMenu() {
        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        //đóng drawer
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        Intent intentDSDK = new Intent(MainActivity.this, DanhSachDangKyActivity.class);
                        startActivity(intentDSDK);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        Intent intentQLSK = new Intent(MainActivity.this, XemSuKienActivity.class);
                        startActivity(intentQLSK);
                        drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

    }
    private void Actionbar() {
        //Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void AnhXa() {



        //listDSDiemDanh.setVisibility(View.INVISIBLE);

        imageViewCauHinh =(ImageButton) findViewById(R.id.imge_user_main_sua);
        imageViewMenu =(ImageButton) findViewById(R.id.imge_user_main_menu);

        toolbar             = (Toolbar) findViewById(R.id.toolBarScan);
        scanBtn             = (Button) findViewById(R.id.buttonScan);
        drawerLayout        = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView      = (NavigationView) findViewById(R.id.navigationViewUser);
        listViewMenu =(ListView) findViewById(R.id.listViewMainNavigation);
        listViewCauHinh =(ListView) findViewById(R.id.listViewCauHinhTaiKhoan);

        arrayCauHinh = new ArrayList<>();
        arrayCauHinh.add(0, new Menu("Cấu hình", R.drawable.user));
        adapterCauHinh = new MenuAdapter(arrayCauHinh, getApplicationContext());
        listViewCauHinh.setAdapter(adapterCauHinh);

        fabSpeedDial = (FabSpeedDial) findViewById(R.id.floatingMenu);


        arrayMenu = new ArrayList<>();
        arrayMenu.add(0, new Menu("Trang chủ", R.drawable.home));
        arrayMenu.add(1,new Menu("Quản lý người dùng",R.drawable.search));
        arrayMenu.add(2, new Menu("Quản lý sự kiện", R.drawable.contact));

        // đưa dữ liệu vào adapter
        adapterMenu = new MenuAdapter(arrayMenu,getApplicationContext());
        // đổi các adapter vào lít view
        listViewMenu.setAdapter(adapterMenu);


        listViewCauHinh.setVisibility(View.INVISIBLE);
        listViewMenu.setVisibility(View.VISIBLE);
        imageViewMenu.setVisibility(View.INVISIBLE);
        imageViewCauHinh.setVisibility(View.VISIBLE);

        // mData = FirebaseDatabase.getInstance().getReference();


//        floatingMenu                        = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
//        floatingButtonDiemDanhBoSung        =  (FloatingActionButton) findViewById(R.id.itemDiemDanhBoSung);
//        floatingButtonDanhSachDaQuet        = (FloatingActionButton) findViewById(R.id.itemDanhSachDaQuet);

    }

    // khởi tạo menu search
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // khởi tạo menu
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // bắt sự kiện cho search
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSearch:
                Intent intentDM = new Intent(MainActivity.this, DanhSachDangKyActivity.class);
                startActivity(intentDM);
        }
        return super.onOptionsItemSelected(item);
    }

    public static JSONObject updateData(String id, String name) {
        com.squareup.okhttp.Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlUpdate+"action=update&id="+id+"&name="+name)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            //Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    class UpdateDataActivity extends AsyncTask<Void, Void, Void> {
        String result=null;

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = updateData("b1507264","Update ne");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result=jsonObject.getString("result");

                }
            } catch (JSONException je) {
                //Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }

    }


    public JSONObject insertData(String id, String name) {
        com.squareup.okhttp.Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(urlUpdate+"action=insert&id="+id+"&name="+name)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            //Log.e(TAG, "recieving null " + e.getLocalizedMessage());
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    class InsertData extends AsyncTask < Void, Void, Void > {
        String result = null;


        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
            JSONObject jsonObject = insertData(mssv, ghiChu);
            //Log.i(Controller.TAG, "Json obj ");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result = jsonObject.getString("result");

                }
            } catch (JSONException je) {
                // Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }


    }

    class QuetDiemDanh extends AsyncTask < Void, Void, Void > {
        String result = null;


        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = insertData(scannedData, null);
            //Log.i(Controller.TAG, "Json obj ");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result = jsonObject.getString("result");

                }
            } catch (JSONException je) {
                // Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            scannedData = result.getContents();
            if (scannedData != null) {
                //new SendRequest().execute();
                new QuetDiemDanh().execute();


            } else {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void DialogDiemDanhSinhVien(){
        final Dialog dialog = new Dialog(this);
        //Bỏ tiêu đề hộp thoại dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Gán layout custom vào màn hình chính(activity_main.xml)
        dialog.setContentView(R.layout.update_diemdanh);
        //Khóa màn hình lúc hộp thoại dialog hiện lên
        dialog.setCanceledOnTouchOutside(false);
        final EditText editTextMssv =  dialog.findViewById(R.id.editTextMSSV);
        final EditText editTextGhiChu =  dialog.findViewById(R.id.editTextGhiChu);
        Button btnDiemDanh =  dialog.findViewById(R.id.buttonAdd);
        Button btnHuy =  dialog.findViewById(R.id.buttonHuy);

        btnDiemDanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mssv = editTextMssv.getText().toString().trim();
                ghiChu = editTextGhiChu.getText().toString().trim();
                if (mssv.isEmpty() && ghiChu.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    new InsertData().execute();
                    dialog.dismiss();
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tắt dialog
                //dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}


