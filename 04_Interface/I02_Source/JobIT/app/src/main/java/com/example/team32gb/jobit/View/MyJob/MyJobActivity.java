package com.example.team32gb.jobit.View.MyJob;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.Applied.AppliedActivity;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.InviteJob.InviteJobActivity;
import com.example.team32gb.jobit.View.SavedJob.SavedJobActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.example.team32gb.jobit.View.WaitingForInterview.InterviewActivity;

public class MyJobActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private Button btnSavedJob;
    private Button btnAppliedJob;
    private Button btnInterviewJob;
    private Button btnMoiLam;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        btnSavedJob = this.findViewById(R.id.btnSavedJob);
        btnAppliedJob = this.findViewById(R.id.btnAppliedJob);
        btnInterviewJob = this.findViewById(R.id.btnInterviewJob);
        btnMoiLam = this.findViewById(R.id.btnMoiLam);

        myToolBar = findViewById(R.id.tb);
        myToolBar.setTitle("Công việc của tôi");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME,MODE_PRIVATE);

        btnSavedJob.setOnClickListener(this);
        btnAppliedJob.setOnClickListener(this);
        btnInterviewJob.setOnClickListener(this);
        btnMoiLam.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.listjob_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
            case R.id.tbHome:
                    Util.jumpActivityRemoveStack(this,HomeJobSeekerActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSavedJob:
                if(sharedPreferences.getBoolean(Config.IS_LOGGED,false)) {
                    Util.jumpActivity(this,SavedJobActivity.class);
                }
                else {
                    Util.jumpActivity(this,SignInActivity.class);
                }
                break;
            case R.id.btnAppliedJob:
                if(sharedPreferences.getBoolean(Config.IS_LOGGED,false)) {
                    Util.jumpActivity(this,AppliedActivity.class);
                }
                else {
                    Util.jumpActivity(this,SignInActivity.class);
                }
                break;
            case R.id.btnInterviewJob:
                if(sharedPreferences.getBoolean(Config.IS_LOGGED,false)) {
                    Util.jumpActivity(this,InterviewActivity.class);
                }
                else {
                    Util.jumpActivity(this,SignInActivity.class);
                }
                break;
            case R.id.btnMoiLam:
                if(sharedPreferences.getBoolean(Config.IS_LOGGED,false)) {
                    Util.jumpActivity(this,InviteJobActivity.class);
                }
                else {
                    Util.jumpActivity(this,SignInActivity.class);
                }
                break;
            default:
                break;
        }

    }
}
