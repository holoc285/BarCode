package com.example.holoc.scanqrcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.SuKien;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class SuKienAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private List<SuKien>listsukien;
  // private List<sukienhienthi>listsukien;
    public SuKienAdapter(Context context, int layout, List<SuKien> listsukien) {
        this.context = context;
        this.layout = layout;
        this.listsukien = listsukien;
    }
    public SuKienAdapter(){

    }

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public int getCount(){
        return listsukien.size();
    }
    @Override
    public Objects getItem(int i){
        return null;
    }
    @Override
    public long getItemId(int i){
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
      //  String tendv = new String("khong co");
        LayoutInflater  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        TextView ten = (TextView) view.findViewById(R.id.ten);
        TextView donvi = (TextView) view.findViewById(R.id.donvi);
        TextView ngay= (TextView) view.findViewById(R.id.ngay);
      //  TextView tgbd = (TextView) view.findViewById(R.id.tgbd);
      //  TextView tgkt = (TextView) view.findViewById(R.id.tgkt);
      //  TextView ghichu = (TextView) view.findViewById(R.id.ghichu);

        ten.setText(listsukien.get(i).getTensk());
        donvi.setText("Đơn vị: "+listsukien.get(i).getTendonvi());
      //  donvi.setText("ĐƠN VỊ:    "+tendv);
        ngay.setText("Ngày: "+listsukien.get(i).getNgay());
    //    tgbd.setText("THỜI GIAN:   "+listsukien.get(i).getTgbatdau());
    //    tgkt.setText("-  "+listsukien.get(i).getTgketthuc());
    //    ghichu.setText("GHI CHÚ:  "+listsukien.get(i).getChuthich());
        return view;
    }


}

