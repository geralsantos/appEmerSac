package com.example.myapplication.ui.tareo_actividades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TareoActividadesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TareoActividadesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}