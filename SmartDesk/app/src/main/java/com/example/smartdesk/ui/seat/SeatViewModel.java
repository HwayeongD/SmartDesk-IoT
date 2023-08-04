package com.example.smartdesk.ui.seat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SeatViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public SeatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is seat fragment");
    }

    public LiveData<String> getText() { return mText; }


}
