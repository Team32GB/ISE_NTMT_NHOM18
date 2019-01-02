package com.example.team32gb.jobit.View.RecentSearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.team32gb.jobit.Adapter.SwipeController;
import com.example.team32gb.jobit.Adapter.SwipeControllerActions;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.Admin.AdminHomeActivity;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.SavedJob.SavedJobActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecentSearch extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar myToolBar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_search);

        recyclerView = findViewById(R.id.rcRecentSearch);
        Paper.init(this);
        myToolBar = findViewById(R.id.tb);

        myToolBar.setTitle("Tìm kiếm gần đây");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        showRecentSearch();
    }

    void showRecentSearch() {
        final List<Search> searches = Paper.book().read("searches");
        if (searches == null) return;
        final RecenteSearchLayoutAdapter adapter = new RecenteSearchLayoutAdapter(this, searches);

        SwipeControllerActions swipeControllerActions = new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                searches.remove(position);
                Paper.book().write("searches",searches);
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
                Util.jumpActivityRemoveStack(this, HomeJobSeekerActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

}
