package com.example.holoc.scanqrcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.Menu;

import java.util.ArrayList;

/**
 * Created by User on 06/06/2018.
 */

public class MenuAdapter extends BaseAdapter {
    private ArrayList<Menu> arrayListMenu;
    Context context;

    public MenuAdapter(ArrayList<Menu> arrayListMenu, Context context) {
        this.arrayListMenu = arrayListMenu;
        this.context = context;
    }

    public MenuAdapter() {
    }

    @Override
    public int getCount() {
        return arrayListMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder
    public class ViewHolder{
        TextView txtmenu;
        ImageView imgmenu;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // nếu chưa có dữ liệu
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            //get service là layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_menu_main_admin, null);
            // ánh xạ tới từng dòng
            viewHolder.txtmenu = (TextView) convertView.findViewById(R.id.textmenu);
            viewHolder.imgmenu = (ImageView) convertView.findViewById(R.id.imagemenu);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // nếu đã có dữ liệu thì getTag
        // lấy dữ liệu từ trong mảng
        // gọi lại khuôn là menu
        Menu menu = (Menu) getItem(position);
        viewHolder.txtmenu.setText(menu.getTen());
        viewHolder.imgmenu.setImageResource(menu.getHinh());
        return convertView;
    }
}
