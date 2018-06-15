package com.example.holoc.scanqrcode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 27/05/2018.
 */

public class UsersViewHolder extends RecyclerView.ViewHolder{
    public View mView;

    public UsersViewHolder(View itemView) {
        super(itemView);
        mView = itemView;


    }




    public void setName(String name){
        TextView userNameView = mView.findViewById(R.id.dong_user_txtTen);
        userNameView.setText(name);
    }


    public void setUserImage(String userImage, Context context){
        CircleImageView circleImageView = mView.findViewById(R.id.dong_user_image);
        Picasso.with(context).load(userImage)
                .placeholder(R.drawable.no_image_available)
                .error(R.drawable.err)
                .into(circleImageView);
    }
    public void setUserMSSV(String mssv){
        TextView userMSSV = mView.findViewById(R.id.dong_user_txtmssv);
        userMSSV.setText(mssv);
    }
    public void setTrangThai(String xacnhan){
        TextView userTrangThai = mView.findViewById(R.id.dong_user_txtXacnhan);
        userTrangThai.setText(xacnhan);

    }

    public String getTrangThai(){
        TextView userTrangThai = mView.findViewById(R.id.dong_user_txtXacnhan);
        return userTrangThai.getText().toString();
    }




}
