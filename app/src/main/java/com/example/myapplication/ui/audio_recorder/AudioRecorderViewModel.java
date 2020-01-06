package com.example.myapplication.ui.audio_recorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AudioRecorderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AudioRecorderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is audio_recorder fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}