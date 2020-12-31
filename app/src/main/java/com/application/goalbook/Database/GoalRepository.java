package com.application.goalbook.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GoalRepository {

    private GoalDao goalDao;
    private LiveData<List<Goal>> allGoals;
    private LiveData<Goal> goalById;

    public GoalRepository(Application application)
    {
        GoalDatabase database = GoalDatabase.getInstance(application);
        goalDao = database.goalDao();
        allGoals = goalDao.getAllGoals();
    }

    public void insert(Goal goal)
    {
        new InsertAsyncTask(goalDao).execute(goal);
    }

    public void update(Goal goal)
    {
        new UpdateAsyncTask(goalDao).execute(goal);
    }

    public void delete(Goal goal)
    {
        new DeleteAsyncTask(goalDao).execute(goal);
    }

    public LiveData<List<Goal>> getAllGoals(){
        return allGoals;
    }

    public LiveData<Goal> getGoalById(int gid){
        return goalDao.getGoalById(gid);
    }


    private static class InsertAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;
        private InsertAsyncTask(GoalDao goalDao)
        {
            this.goalDao = goalDao;
        }
        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.insert(goals[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;
        private UpdateAsyncTask(GoalDao goalDao)
        {
            this.goalDao = goalDao;
        }
        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.update(goals[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;
        private DeleteAsyncTask(GoalDao goalDao)
        {
            this.goalDao = goalDao;
        }
        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.delete(goals[0]);
            return null;
        }
    }


}
