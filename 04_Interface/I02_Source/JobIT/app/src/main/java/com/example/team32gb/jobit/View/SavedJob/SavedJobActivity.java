package com.example.team32gb.jobit.View.SavedJob;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.team32gb.jobit.Adapter.SwipeController;
import com.example.team32gb.jobit.Adapter.SwipeControllerActions;
import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Presenter.SavedJob.PresenterSavedJob;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.WaitingForInterview.ListJobInterviewViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SavedJobActivity extends AppCompatActivity implements ViewListSavedJob{
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private PresenterSavedJob presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_job);

        recyclerView = this.findViewById(R.id.rvListInterview);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(Config.IS_ACTIVITY_APPLY,false);
        editor.apply();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myToolBar = findViewById(R.id.tbListInterview);
        myToolBar.setTitle("Đã lưu");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new PresenterSavedJob(this);
        presenter.onCreate();
        presenter.getListJob(FirebaseAuth.getInstance().getUid());

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
                Log.e("kiemtraback","ok");
               onBackPressed();
               break;
            case R.id.tbHome:
                Util.jumpActivityRemoveStack(this,HomeJobSeekerActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showListJob(final List<ItemPostJob> itemPostJobs) {
        Log.e("kiemtradetail", "11:" + itemPostJobs.get(0).getDataPostJob().getIdCompany() + itemPostJobs.get(0).getDataPostJob().getNameCompany());
        final ListJobInterviewViewAdapter adapter = new ListJobInterviewViewAdapter(this,itemPostJobs);

        SwipeControllerActions swipeControllerActions = new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
                String uid = FirebaseAuth.getInstance().getUid();
                Log.e("kiemtraDelete",position + "");
                String idCompany = itemPostJobs.get(position).getDataPostJob().getIdCompany();
                String idJob = itemPostJobs.get(position).getDataPostJob().getIdJob();
                Util.RemoveSaveJob(SavedJobActivity.this,nodeRoot,uid,idCompany,idJob);
                itemPostJobs.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
            }
        };

        recyclerView.setAdapter(adapter);

        final SwipeController swipeController = new SwipeController(swipeControllerActions);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);

        itemTouchHelper.attachToRecyclerView(recyclerView);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}
