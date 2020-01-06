package com.example.myapplication;

import android.os.Environment;

public class config {
    final String rutaArchivos=Environment.getExternalStorageDirectory().toString()+"/appEmerSac/";

    public String getRutaArchivos() {
        return rutaArchivos;
    }
}
