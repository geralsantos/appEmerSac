package com.example.myapplication.ui.actividades_registradas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActividadesRegistradasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActividadesRegistradasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is actividades_registradas fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}