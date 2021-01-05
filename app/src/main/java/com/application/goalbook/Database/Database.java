package com.application.goalbook.Database;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;

@androidx.room.Database(entities = {Goal.class,Profile.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract GoalDao goalDao();
    public abstract ProfileDao profileDao();

    public static synchronized Database getInstance(Context context){
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,"goal_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    //for populating database with some inital value
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //adding default profile to app database
            String name = Constants.DEFAULT_NAME;
            String purpose = Constants.DEFAULT_PURPOSE;
            String vision = Constants.DEFAULT_VISION;
            String mission = Constants.DEFAULT_MISSION;
            String colorPreference = Constants.DEFAULT_COLOR;
            Boolean showNotification = false;
            Profile defaultProfile = new Profile(name,purpose,mission,vision,null,colorPreference,showNotification,null);

            new PopulateDbAsyncTask(instance).execute(defaultProfile);
        }
    };



    private static class PopulateDbAsyncTask extends AsyncTask<Profile,Void,Void>
    {
        private ProfileDao profileDao;
        private PopulateDbAsyncTask(Database db){
            profileDao = db.profileDao();
        }
        @Override
        protected Void doInBackground(Profile... profiles) {

            profileDao.insert(profiles[0]);
            return null;
        }
    }
}
