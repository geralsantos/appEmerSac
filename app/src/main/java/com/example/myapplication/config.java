package com.example.myapplication;

import android.os.Environment;

public class config {
    //Environment.getExternalStoragePublicDirectory(
    //            Environment.DIRECTORY_PICTURES).getAbsolutePath();
    final String rutaArchivos= Environment.getExternalStorageDirectory().toString()+"/appEmerSac/";

    public String getRutaArchivos() {
        return rutaArchivos;
    }
}
