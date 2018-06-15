package com.example.holoc.scanqrcode.model;

/**
 * Created by Rainy on 5/27/2018.
 */

public class DonVi {
    public int madv;

    public String tendv;


    public DonVi(int madv, String tendv) {
        this.madv = madv;
        this.tendv = tendv;
    }


    public int getMadv() {
        return madv;
    }

    public void setMadv(int madv) {
        this.madv = madv;
    }

    public String getTendv() {
        return tendv;
    }

    public void setTendv(String tendv) {
        this.tendv = tendv;
    }
}
