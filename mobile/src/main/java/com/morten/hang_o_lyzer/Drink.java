package com.morten.hang_o_lyzer;

import android.graphics.drawable.Drawable;

/**
 * Created by Morten on 10-05-2016.
 */
public class Drink {
    public String name;
    public Drawable img;
    public Double pct;
    public int size;

    public Drink(String name, Drawable img, Double pct, int size) {
        this.name = name;
        this.img = img;
        this.pct = pct;
        this.size = size;
    }
}
