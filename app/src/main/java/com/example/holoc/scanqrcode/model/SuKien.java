package com.example.holoc.scanqrcode.model;

import java.io.Serializable;

/**
 * Created by Rainy on 5/27/2018.
 */

public class SuKien implements Serializable {
    public String tensk;
    public int madv;
    public String ngay;
    public String tgbatdau;
    public String tgketthuc;
    public String chuthich;
    public String tendonvi;
    public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTendonvi() {
        return tendonvi;
    }

    public void setTendonvi(String tendonvi) {
        this.tendonvi = tendonvi;
    }


    public String getTensk() {
        return tensk;
    }

    public void setTensk(String tensk) {
        this.tensk = tensk;
    }

    public int getMadv() {
        return madv;
    }

    public void setMadv(int madv) {
        this.madv = madv;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getTgbatdau() {
        return tgbatdau;
    }

    public void setTgbatdau(String tgbatdau) {
        this.tgbatdau = tgbatdau;
    }

    public String getTgketthuc() {
        return tgketthuc;
    }

    public void setTgketthuc(String tgketthuc) {
        this.tgketthuc = tgketthuc;
    }

    public String getChuthich() {
        return chuthich;
    }

    public void setChuthich(String chuthich) {
        this.chuthich = chuthich;
    }



    public SuKien(){


    }


    public SuKien(String ten, int ma, String ngaydr, String tgbd, String tgkt, String ct){
        tensk = ten;
        madv = ma;
        ngay = ngaydr;
        tgbatdau = tgbd;
        tgketthuc = tgkt;
        chuthich = ct;
    }

    public SuKien(String key, String tensk, String tendonvi, String ngay, String tgbatdau, String tgketthuc, String chuthich) {
        this.key = key;
        this.tensk = tensk;
        this.tendonvi = tendonvi;
        this.ngay = ngay;
        this.tgbatdau = tgbatdau;
        this.tgketthuc = tgketthuc;
        this.chuthich = chuthich;
    }


}
