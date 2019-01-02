package com.example.team32gb.jobit.Presenter.ListJobSearch;

import android.util.Log;

import com.example.team32gb.jobit.Lib.GreenRobotEventBus;
import com.example.team32gb.jobit.Model.ListJobSearch.ItemJob;
import com.example.team32gb.jobit.Model.ListJobSearch.ModelListJobSearch;
import com.example.team32gb.jobit.Model.ListJobSearch.DataJob;
import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.View.ListJobSearch.ViewListJobSearch;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class PresenterLogicListJobSearch implements PresenterInListJobSearch {
    ViewListJobSearch view;
    ModelListJobSearch model;
    GreenRobotEventBus eventBus;

    public PresenterLogicListJobSearch(ViewListJobSearch view){
        this.view = view;
        model = new ModelListJobSearch();
        eventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void getListJob(String timKiem, String diaDiem) {
        model.getListJob(timKiem,diaDiem);
    }


    @Override
    @Subscribe
    public void showListJob(List<ItemPostJob> itemPostJobs) {
        Log.e("kiemtraAlgolia","111");
        view.showListJob(itemPostJobs);
    }

}
