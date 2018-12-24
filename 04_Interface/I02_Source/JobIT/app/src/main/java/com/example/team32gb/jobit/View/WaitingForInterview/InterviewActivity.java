package com.example.team32gb.jobit.View.WaitingForInterview;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Presenter.WaitingForInterviewNTV.PresenterInInterview;
import com.example.team32gb.jobit.Presenter.WaitingForInterviewNTV.PresenterInterview;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class InterviewActivity extends AppCompatActivity implements ViewListJobInterview {

    private Toolbar myToolBar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private PresenterInInterview presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        myToolBar = findViewById(R.id.tbListInterview);
        recyclerView = this.findViewById(R.id.rvListInterview);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(Config.IS_ACTIVITY_APPLY,false);
        editor.apply();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myToolBar.setTitle("Chờ phỏng vấn");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new PresenterInterview(this);
        presenter.onCreate();
        presenter.getListJob(FirebaseAuth.getInstance().getUid());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
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
                break;
            case R.id.tbHome:
                Util.jumpActivityRemoveStack(this,HomeJobSeekerActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showListJob(List<ItemPostJob> itemPostJobs) {
        ListJobInterviewViewAdapter adapter = new ListJobInterviewViewAdapter(this,itemPostJobs);
        recyclerView.setAdapter(adapter);
    }
}
