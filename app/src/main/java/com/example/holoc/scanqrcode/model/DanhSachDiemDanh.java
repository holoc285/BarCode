package com.example.holoc.scanqrcode.model;

/**
 * Created by holoc on 6/3/2018.
 */

public class DanhSachDiemDanh {

    private String mssv;
    private String gioDiemDanh;
    private String ghiChu;

    public DanhSachDiemDanh(String mssv, String gioDiemDanh, String ghiChu) {
        this.mssv = mssv;
        this.gioDiemDanh = gioDiemDanh;
        this.ghiChu = ghiChu;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getGioDiemDanh() {
        return gioDiemDanh;
    }

    public void setGioDiemDanh(String gioDiemDanh) {
        this.gioDiemDanh = gioDiemDanh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
