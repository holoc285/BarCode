package com.example.holoc.scanqrcode.model;

/**
 * Created by User on 06/06/2018.
 */

public class Menu {
    private String ten;
    private int hinh;

    public Menu() {
    }

    public Menu(String ten, int hinh) {
        this.ten = ten;
        this.hinh = hinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }
}
