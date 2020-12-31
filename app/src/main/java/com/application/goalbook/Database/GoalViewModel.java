package com.application.goalbook.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private GoalRepository repository;
    private LiveData<List<Goal>> allGoals;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        repository = new GoalRepository(application);
        allGoals = repository.getAllGoals();
    }

    public void insert(Goal goal)
    {
        repository.insert(goal);
    }

    public void update(Goal goal)
    {
        repository.update(goal);
    }

    public void delete(Goal goal)
    {
        repository.delete(goal);
    }

    public LiveData<List<Goal>> getAllGoals(){
        return allGoals;
    }

    public LiveData<Goal> getGoalById(int gid)
    {
        return repository.getGoalById(gid);
    }

}
