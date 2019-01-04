package com.example.team32gb.jobit.View.Applied;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.team32gb.jobit.Adapter.SwipeController;
import com.example.team32gb.jobit.Adapter.SwipeControllerActions;
import com.example.team32gb.jobit.Model.Applied.ItemJobApplied;
import com.example.team32gb.jobit.Model.ListJobSearch.DataJob;
import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Presenter.Applied.PresenterApplied;
import com.example.team32gb.jobit.Presenter.Applied.PresenterInApplied;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.SavedJob.SavedJobActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AppliedActivity extends AppCompatActivity implements ViewListJobApplied {

    private Toolbar myToolBar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private List<DataJob> lsData;
    private PresenterInApplied presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(Config.IS_ACTIVITY_APPLY,false);
        editor.apply();

        myToolBar = findViewById(R.id.tbListApplied);
        recyclerView = this.findViewById(R.id.rvListApplied);
//        i/nitData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myToolBar.setTitle("Đã apply");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new PresenterApplied(this);
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
    public void showListJob(final List<ItemPostJob> itemPostJobs) {
        Log.e("kiemtraaaplied","Heeeeellllooo");
        final ListJobAppliedViewAdapter adapter = new ListJobAppliedViewAdapter(this,itemPostJobs);
        SwipeControllerActions swipeControllerActions = new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
                DatabaseReference drChoPVNTV = nodeRoot.child("Applieds").child(FirebaseAuth.getInstance().getUid())
                                        .child(itemPostJobs.get(position).getDataPostJob().getIdCompany())
                                        .child(itemPostJobs.get(position).getDataPostJob().getIdJob());
                DatabaseReference dr = nodeRoot.child("loaiPhongVans").child(FirebaseAuth.getInstance().getUid())
                        .child(itemPostJobs.get(position).getDataPostJob().getIdCompany())
                        .child(itemPostJobs.get(position).getDataPostJob().getIdJob());

                drChoPVNTV.removeValue();
                dr.removeValue();

                itemPostJobs.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
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
