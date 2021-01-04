package com.application.goalbook.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

public class ProfileRepository {
    private ProfileDao profileDao;
    private LiveData<Profile> profileLiveData;

    public ProfileRepository(Application application)
    {
        Database database = Database.getInstance(application);
        profileDao = database.profileDao();
        profileLiveData = profileDao.getProfileLiveData();
    }

    public void insert(Profile profile)
    {
        new InsertProfileAsyncTask(profileDao).execute(profile);
    }

    public void update(Profile profile)
    {
        new UpdateProfileAsyncTask(profileDao).execute(profile);
    }

    public LiveData<Profile> getProfileLiveData()
    {
        return profileLiveData;
    }

    class InsertProfileAsyncTask extends AsyncTask<Profile,Void,Void>
    {
        private ProfileDao profileDao;
        InsertProfileAsyncTask(ProfileDao profileDao)
        {
            this.profileDao = profileDao;
        }
        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.insert(profiles[0]);
            return null;
        }
    }

    class UpdateProfileAsyncTask extends AsyncTask<Profile,Void,Void>
    {
        private ProfileDao profileDao;
        UpdateProfileAsyncTask(ProfileDao profileDao)
        {
            this.profileDao = profileDao;
        }
        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.update(profiles[0]);
            return null;
        }
    }
}
