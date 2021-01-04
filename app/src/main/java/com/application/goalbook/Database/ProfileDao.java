package com.application.goalbook.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface ProfileDao {

    @Insert
    void insert (Profile profile);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Profile profile);

    @Query("SELECT * FROM profile_table")
    LiveData<Profile> getProfileLiveData();
}
