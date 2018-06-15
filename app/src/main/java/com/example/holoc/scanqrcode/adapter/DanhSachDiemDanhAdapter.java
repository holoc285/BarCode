package com.example.holoc.scanqrcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.DanhSachDiemDanh;

import java.util.List;

/**
 * Created by holoc on 6/3/2018.
 */

public class DanhSachDiemDanhAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<DanhSachDiemDanh> diemDanhList;

    public DanhSachDiemDanhAdapter(Context context, int layout, List<DanhSachDiemDanh> diemDanhList) {
        this.context = context;
        this.layout = layout;
        this.diemDanhList = diemDanhList;
    }

    @Override
    public int getCount() {
        return diemDanhList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        TextView txtMssv = view.findViewById(R.id.textViewMssv);
        TextView txtGioDiemDanh = view.findViewById(R.id.textViewGioDiemDanh);
        TextView txtGhiChu = view.findViewById(R.id.textViewGhiChu);
        DanhSachDiemDanh danhSachDiemDanh = diemDanhList.get(i);

        //Gan gia tri cho ListView
        txtMssv.setText(danhSachDiemDanh.getMssv());
        txtGioDiemDanh.setText(danhSachDiemDanh.getGioDiemDanh());
        txtGhiChu.setText(danhSachDiemDanh.getGhiChu());
        return view;
    }
}
