package com.example.team32gb.jobit.View.ListJobSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.team32gb.jobit.Model.ListJobSearch.DataJob;
import com.example.team32gb.jobit.Model.ListJobSearch.ItemJob;
import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Presenter.ListJobSearch.PresenterInListJobSearch;
import com.example.team32gb.jobit.Presenter.ListJobSearch.PresenterLogicListJobSearch;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.View.Admin.AdminHomeActivity;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.ListJob.ListJobViewAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListJobSearchActivity extends AppCompatActivity implements ViewListJobSearch {
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private PresenterInListJobSearch presenter;
    private ProgressDialog progressDialog;
    private String timKiem = "";
    private String diaDiem = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_job_search);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(Config.IS_ACTIVITY_APPLY,true);
        editor.apply();

        myToolBar = findViewById(R.id.tbListJobSearch);
        recyclerView = this.findViewById(R.id.rvListJobSearch);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        myToolBar.setTitle("Tìm việc");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        presenter = new PresenterLogicListJobSearch(this);
        presenter.onCreate();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        timKiem = bundle.getString("timKiem");
        diaDiem = bundle.getString("diaDiem");
//        Log.e("kiemtraBundle",timKiem + ":" + diaDiem);


    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getListJob(timKiem,diaDiem);
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
        switch (item.getItemId()){
            case R.id.tbHome:
                Intent intent = new Intent(this, HomeJobSeekerActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showListJob(List<ItemPostJob> itemPostJobs) {
        Log.e("kiemtraAlgolia", "Ok");
        Log.e("kiemtraalgolia",itemPostJobs.size() + " : " + itemPostJobs.get(0).getDataPostJob().getNameJob());
        ListJobViewAdapter adapter = new ListJobViewAdapter(this,itemPostJobs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this,R.anim.layout_animation_fall_down);
        recyclerView.setLayoutManager(layoutManager);
        progressDialog.dismiss();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutAnimation(animation);
    }
}
