package com.application.goalbook.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository repository;
    private LiveData<Profile> profileLiveData;


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        profileLiveData = repository.getProfileLiveData();
    }

    public void insert(Profile profile)
    {
        repository.insert(profile);
    }

    public void update(Profile profile)
    {
        repository.update(profile);
    }

    public LiveData<Profile> getProfileLiveData(){
        return profileLiveData;
    }
}
