package com.example.team32gb.jobit;

import androidx.annotation.NonNull;

import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.WaitingAcceptNTD.WaitingAcceptFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.team32gb.jobit.View.PostedJob.PostedFragment;

public class JobRecruitmentActivity extends AppCompatActivity {
  private Toolbar myToolBar;
    private ActionBar actionBar;
    private FrameLayout mainFrag;
    private BottomNavigationView mainNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_recruitment);

        mainFrag = findViewById(R.id.mainFrag);
        mainNav = findViewById(R.id.mainNav);

        myToolBar = findViewById(R.id.tbJobRecruitment);
        myToolBar.setTitle("Danh sách việc làm ");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setFragment(new PostedFragment());

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navPosted:
                        setFragment(new PostedFragment());
                        return true;
                    case R.id.navExpire:
                        setFragment(new ExpireFragment());
                        return true;
                        default:
                            return false;
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detailjob_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
            case R.id.tbHome:
                Util.jumpActivityRemoveStack(this,HomeRecruitmentActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrag,fragment);
        fragmentTransaction.commit();
    }
}
