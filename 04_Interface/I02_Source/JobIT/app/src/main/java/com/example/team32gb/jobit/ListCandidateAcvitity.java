package com.example.team32gb.jobit;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;

import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.team32gb.jobit.Utility.Util;

public class ListCandidateAcvitity extends AppCompatActivity {
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private TextView txtNameJob, txtTimeJob;
    private FrameLayout mainFrag;
    private BottomNavigationView mainNav;
    private String nameJob = "", timeJob = "",idCompany,idJob;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidate);


        myToolBar = findViewById(R.id.tbListCandidate);
        txtNameJob = this.findViewById(R.id.txtNameJob);
        txtTimeJob = this.findViewById(R.id.txtTime);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        if(bundle != null) {
            nameJob = bundle.getString("nameJob");
            timeJob = bundle.getString("timeJob");
            idCompany = bundle.getString("idCompany");
            idJob = bundle.getString("idJob");
            Log.e("kiemtraDetail",idJob);

            txtNameJob.setText(nameJob);
           // Util.setSubTime(timeJob,txtTimeJob);
        }

        myToolBar.setTitle("");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainFrag = findViewById(R.id.mainFrag);
        mainNav = findViewById(R.id.mainNav);
        CandidatePostedFragment candidatePostedFragment = new CandidatePostedFragment();
        candidatePostedFragment.showList(idCompany,idJob);
        setFragment(candidatePostedFragment);
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
              switch (menuItem.getItemId()){
                  case R.id.navPosted:
                      CandidatePostedFragment candidatePostedFragment = new CandidatePostedFragment();
                      candidatePostedFragment.showList(idCompany,idJob);
                      setFragment(candidatePostedFragment);
                      return true;
                  case R.id.navWaitingInterview:
                      WaitingInterviewFragment waitingInterviewFragment = new WaitingInterviewFragment();
                      waitingInterviewFragment.showList(idCompany,idJob);
                      setFragment(waitingInterviewFragment);
                      return true;
                  case R.id.navInviteJob:
                      InviteJobFragment inviteJobFragment = new InviteJobFragment();
                      inviteJobFragment.showList(idCompany,idJob);
                      setFragment(inviteJobFragment);
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
                Util.jumpActivityRemoveStack(this,HomeRecruitmentActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrag,fragment);
        fragmentTransaction.commit();
    }
}
