package com.example.team32gb.jobit.Presenter.CreateCV;

import com.example.team32gb.jobit.Model.CreateCV.CVEmployeeModel;
import com.example.team32gb.jobit.Model.CreateCV.ProjectInCVModel;

import java.util.List;

public interface PresenterInCreateCV {
    void onCreate();
    void onDestroy();

     void saveCV(String uid, CVEmployeeModel cvEmployeeModel, List<ProjectInCVModel> projectInCVModels);
     void showCV (CVEmployeeModel cvEmployeeModel);
     void getCVFromUid(String uid);
     void getCVFromUid(String uid,CreateCVInterface createCVInterface);
}
