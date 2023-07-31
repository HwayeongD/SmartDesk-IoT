package com.example.smartdesk.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mTextTime;
    private final MutableLiveData<String> mTextContent;

    public HomeViewModel() {
        mTextTime = new MutableLiveData<>();
        mTextContent = new MutableLiveData<>();
        mTextTime.setValue("10A.M.");
        mTextContent.setValue("디자인팀과의 회의");
    }

    public LiveData<String> getTextTime() {
        return mTextTime;
    }

    public LiveData<String> getTextContent() {
        return mTextContent;
    }
}