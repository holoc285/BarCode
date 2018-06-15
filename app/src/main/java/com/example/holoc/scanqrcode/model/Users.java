package com.example.holoc.scanqrcode.model;

import java.io.Serializable;

/**
 * Created by User on 27/05/2018.
 */

public class Users implements Serializable {
    private String name;
    private String mssv;
    private String image;
    private String donvi;
    private String sdt;
    private String xacnhan;
    private String quyen;
    private String device_token;

    public Users() {
    }

    public Users(String name, String mssv, String image, String donvi, String sdt, String xacnhan, String quyen, String device_token) {
        this.name = name;
        this.mssv = mssv;
        this.image = image;
        this.donvi = donvi;
        this.sdt = sdt;
        this.xacnhan = xacnhan;
        this.quyen = quyen;
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getXacnhan() {
        return xacnhan;
    }

    public void setXacnhan(String xacnhan) {
        this.xacnhan = xacnhan;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
