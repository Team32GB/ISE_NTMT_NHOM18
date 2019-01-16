package com.example.team32gb.jobit.View.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.View.Feedback.FeedbackModel;
import com.google.firebase.auth.internal.FederatedSignInActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

import static com.example.team32gb.jobit.Utility.Config.FEED_BACKS_NODE;

public class AdminFeedback extends AppCompatActivity {

    RecyclerView rvListFeedback;
    ArrayList<FeedbackModel> dataArray = new ArrayList<>();


    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private ActionBar acitonBar;
    private AdminFeedbackAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);
        rvListFeedback = findViewById(R.id.rvListFeedback);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải dữ liệu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        onGetData();

        toolbar = findViewById(R.id.tbFeedback);
        setSupportActionBar(toolbar);
        acitonBar = getSupportActionBar();
        acitonBar.setDisplayHomeAsUpEnabled(true);

        adaptor = new AdminFeedbackAdaptor(dataArray);
        rvListFeedback.setAdapter(adaptor);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(AdminFeedback.this);
        rvListFeedback.setLayoutManager(layoutManager);
        rvListFeedback.setHasFixedSize(true);

    }

   public void onGetData(){
       final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FEED_BACKS_NODE);
       ref.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot data : dataSnapshot.getChildren()){
                   for (DataSnapshot itemData : data.getChildren()){
                       FeedbackModel model = itemData.getValue(FeedbackModel.class);
                       //Log.e("kiemtra feedback:", "name"+ model.getContent());
                       adaptor.dataArray.add(model);
                       adaptor.notifyDataSetChanged();

                       progressDialog.dismiss();
                   }

//                   Log.e("kiemtra feedback", "id: " + data.getValue() + "String id: "+ (String)data.getValue() );
//                   String id = (String) data.getValue();    //lấy id của người gửi feedbacks
//
//                   ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {  //Lấy feedbacks
//                       @Override
//                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                           FeedbackModel model = dataSnapshot.getValue(FeedbackModel.class);
//                           dataArray.add(model);
//                           progressDialog.dismiss();
//                       }
//
//                       @Override
//                       public void onCancelled(@NonNull DatabaseError databaseError) {
//                       }
//                   });
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeAdmin:
                Intent intent = new Intent(this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
