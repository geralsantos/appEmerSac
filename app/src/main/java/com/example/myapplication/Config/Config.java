package com.example.myapplication.Config;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    //private static final String BASEURL = "http://138.68.49.231/emer/api/";
    private static final String BASEURL = "http://seguimiento.emerperu.com/api/";
    //private static final String BASEURL = "http://192.168.0.51:8000/api/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){
        if (retrofit==null){
            retrofit = new retrofit2.Retrofit
                    .Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void Mensaje(Context context, String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }
}
