package com.example.team32gb.jobit.Presenter.CreateCV;

import com.example.team32gb.jobit.Lib.EventBus;
import com.example.team32gb.jobit.Lib.GreenRobotEventBus;
import com.example.team32gb.jobit.Model.CreateCV.CVEmployeeModel;
import com.example.team32gb.jobit.Model.CreateCV.ModelCreateCV;
import com.example.team32gb.jobit.Model.CreateCV.ProjectInCVModel;
import com.example.team32gb.jobit.View.CreateCV.ViewCreateCV;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class PresenterLogicCreateCV implements PresenterInCreateCV{
    private ModelCreateCV modelCreateCV;
    private ViewCreateCV viewCreateCV;
    private EventBus eventBus;
    public PresenterLogicCreateCV(ViewCreateCV viewCreateCV){
        this.viewCreateCV = viewCreateCV;
        this.eventBus = GreenRobotEventBus.getInstance();
        modelCreateCV = new ModelCreateCV();
    }
    @Override
    public void getCVFromUid(String uid) {
        CreateCVInterface createCVInterface = new CreateCVInterface() {
            @Override
            public void getCVModel(CVEmployeeModel model) {
                showCV(model);
            }
        };
        modelCreateCV.getCVFromUid(uid,createCVInterface);
    }

    @Override
    public void getCVFromUid(String uid, CreateCVInterface createCVInterface) {

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
    public void saveCV(String uid, CVEmployeeModel cvEmployeeModel, List<ProjectInCVModel> projectInCVModels) {
            modelCreateCV.saveCV(uid,cvEmployeeModel,projectInCVModels);
    }

    @Override
    @Subscribe
    public void showCV(final CVEmployeeModel cvEmployeeModel) {
        viewCreateCV.showCV(cvEmployeeModel);
    }


}
