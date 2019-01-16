package com.example.team32gb.jobit.Model.CreateCV;

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.team32gb.jobit.Lib.GreenRobotEventBus;
import com.example.team32gb.jobit.Presenter.CreateCV.CreateCVInterface;
import com.example.team32gb.jobit.Utility.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    public void getCVFromUid (String uid, final CreateCVInterface createCVInterface){
        DatabaseReference dfCV = dfCVsNode.child(uid);
        dfCV.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG,dataSnapshot + "");
                if(dataSnapshot.getValue() != null) {
                    cvEmployeeModel = dataSnapshot.getValue(CVEmployeeModel.class);
                    createCVInterface.getCVModel(cvEmployeeModel);
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
        Log.e("kiemtramale", uid + ":" + cvEmployeeModel.getIsMale() + " saveCV");
        dfCVsNode.child(uid).setValue(cvEmployeeModel);
    }

}
