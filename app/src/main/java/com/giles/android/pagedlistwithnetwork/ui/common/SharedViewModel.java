package com.giles.android.pagedlistwithnetwork.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

public class SharedViewModel extends ViewModel{

    private final MutableLiveData<String> mUsername = new MutableLiveData<String>();

    @Inject
    public SharedViewModel() {
        super();
    }

    public void setUsername(String username) {
        mUsername.setValue(username);
    }

    public LiveData<String> getUsername() {
        return mUsername;
    }

}
