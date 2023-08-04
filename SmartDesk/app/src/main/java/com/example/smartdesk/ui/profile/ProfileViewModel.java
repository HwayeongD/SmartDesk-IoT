package com.example.smartdesk.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> mDeskHeight;

    public ProfileViewModel() {
        mDeskHeight = new MutableLiveData<>();
        mDeskHeight.setValue("0cm");
    }

    public LiveData<String> getDeskHeight() { return mDeskHeight; }
}
