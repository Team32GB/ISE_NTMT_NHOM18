package com.example.team32gb.jobit.Model.CreateCV;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.team32gb.jobit.Lib.GreenRobotEventBus;
import com.example.team32gb.jobit.Utility.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ModelCreateCV {
    private CVEmployeeModel cvEmployeeModel;
    private List<ProjectInCVModel> projectInCVModels;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dfCVsNode;
    GreenRobotEventBus eventBus;

    String TAG = "kiemtraCV";

    public ModelCreateCV() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dfCVsNode = firebaseDatabase.getReference().child(Config.REF_CVS_NODE);
        eventBus = GreenRobotEventBus.getInstance();
    }
    public void getCVFromUid (String uid){
        DatabaseReference dfCV = dfCVsNode.child(uid);
        dfCV.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG,dataSnapshot + "");
                if(dataSnapshot.getValue() != null) {
                    cvEmployeeModel = dataSnapshot.getValue(CVEmployeeModel.class);
                    eventBus.post(cvEmployeeModel);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getCurrentUserCV(){
    }
    public void saveCV(String uid, CVEmployeeModel cvEmployeeModel, List<ProjectInCVModel> projectInCVModels) {
        dfCVsNode.child(uid).setValue(cvEmployeeModel);
//        DatabaseReference dfProject1 = dfCV.child("projects").child("project1");
//        dfProject1.setValue(projects.get(0));
//        DatabaseReference dfProject2 = dfCV.child("projects").child("project2");
//        dfProject2.setValue(projects.get(1));

    }

}
