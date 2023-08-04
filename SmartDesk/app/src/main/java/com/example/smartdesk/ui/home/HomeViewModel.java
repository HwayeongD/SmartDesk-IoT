package com.example.smartdesk.ui.home;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mNickname;
    private final MutableLiveData<String> mMySeat;
    private final MutableLiveData<String> mMyDeskHeight;

    public HomeViewModel() {
        mNickname = new MutableLiveData<>();
        mMySeat = new MutableLiveData<>();
        mMyDeskHeight = new MutableLiveData<>();
    }

    public LiveData<String> getNickname() {
        return mNickname;
    }

    public LiveData<String> getMySeat() {
        return mMySeat;
    }

    public LiveData<String> getMyDeskHeight() { return mMyDeskHeight; }
}