package com.example.myapplication.Config;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    private static final String BASEURL = "http://148.72.23.133:8000/api/";
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
