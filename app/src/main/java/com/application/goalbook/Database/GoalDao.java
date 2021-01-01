package com.application.goalbook.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert
    void insert (Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("SELECT * FROM goal_table ORDER BY end_date ASC")
    LiveData<List<Goal>> getAllGoals();

    @Query("SELECT * FROM goal_table WHERE gid= (:gid)")
    LiveData<Goal> getGoalById(int gid);
}
