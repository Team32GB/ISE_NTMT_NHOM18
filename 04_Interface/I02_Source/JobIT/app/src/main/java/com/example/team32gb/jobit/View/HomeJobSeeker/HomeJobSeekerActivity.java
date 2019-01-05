package com.example.team32gb.jobit.View.HomeJobSeeker;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.CreateCV.CreateCVActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.ProfileUser.ProfileUserActivity;
import com.example.team32gb.jobit.View.ListJobSearch.ListJobSearchActivity;
import com.example.team32gb.jobit.View.MyJob.MyJobActivity;
import com.example.team32gb.jobit.View.RecentSearch.RecentSearch;
import com.example.team32gb.jobit.View.RecentSearch.RecenteSearchAdapter;
import com.example.team32gb.jobit.View.RecentSearch.Search;
import com.example.team32gb.jobit.View.SelectUserType.SelectUserTypeActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeJobSeekerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button btnSearch;
    private Button btnSignIn;
    private Button btnRecent;
    private Button btnCreateCV;
    private Button btnMyJob;
    private Button btnAccount;
    private Button btnSignOut;
    private Button btnChangeUserType;
    private CheckBox cbMore;
    private AppCompatAutoCompleteTextView edtTimKiem, edtDiaDiem;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        edtDiaDiem = findViewById(R.id.edtDiaDiem);
        dialog = new Dialog(this);

        btnSearch = findViewById(R.id.btnSearch);
        btnSignIn = findViewById(R.id.btnDangNhap);
        btnCreateCV = findViewById(R.id.btnCV);
        btnMyJob = findViewById(R.id.btnMyJob);
        btnAccount = findViewById(R.id.btnAccount);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnRecent = findViewById(R.id.btnRecent);
        btnChangeUserType = findViewById(R.id.btnChangeUserType);
        cbMore = findViewById(R.id.cbMore);
        recyclerView = findViewById(R.id.rcRecent);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Paper.init(this);

        String countries[] = getResources().getStringArray(R.array.TinhThanh);
        ArrayAdapter adapterProvince = new ArrayAdapter(this,android.R.layout.simple_list_item_1,countries);
        edtDiaDiem.setAdapter(adapterProvince);
        edtDiaDiem.setThreshold(1);

        String searchs[] = getResources().getStringArray(R.array.searchs);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,searchs);
        edtTimKiem.setAdapter(adapter);
        edtTimKiem.setThreshold(1);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if(sharedPreferences.getBoolean(Config.IS_LOGGED,false)) {
            btnAccount.setText(sharedPreferences.getString(Config.EMAIL_USER,"User name"));
        }

        btnSearch.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnCreateCV.setOnClickListener(this);
        btnMyJob.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        cbMore.setOnCheckedChangeListener(this);
        btnChangeUserType.setOnClickListener(this);
        btnRecent.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showRecentSearch();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Config.USER_TYPE,Config.IS_JOB_SEEKER);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth.getCurrentUser() != null) {
            btnSignIn.setVisibility(View.GONE);
            btnAccount.setVisibility(View.VISIBLE);
        } else {
            btnAccount.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void setUpDialogReport() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.lost_internet, null);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isConnectingToInternet(HomeJobSeekerActivity.this)) {
                    dialog.show();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(dialogView);
        dialog.setTitle("Mất kết nối internet");
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSearch:
                if(!Util.isConnectingToInternet(this)) {
                    setUpDialogReport();
                } else {
                    Intent intent = new Intent(this, ListJobSearchActivity.class);
                    Bundle bundle = new Bundle();
                    String timKiem = edtTimKiem.getText().toString();
                    String diaDiem = edtDiaDiem.getText().toString();
                    if (timKiem.trim().length() <= 0 && diaDiem.trim().length() <= 0) {
                        edtTimKiem.setError("Hãy nhập thông tin tìm kiếm");
                        edtDiaDiem.setError("Hãy nhập thông tin tìm kiếm");
                    } else {
                        bundle.putString("timKiem", edtTimKiem.getText().toString());
                        bundle.putString("diaDiem", edtDiaDiem.getText().toString());
                        saveRecentSearch(new Search(timKiem, diaDiem));
                        intent.putExtra("bundle", bundle);
                        this.startActivity(intent);
                    }
                }
                break;
            case R.id.btnDangNhap:
                Intent intentSI = new Intent(this, SignInActivity.class);
                startActivity(intentSI);
                break;
            case R.id.btnRecent:
                Util.jumpActivity(this,RecentSearch.class);
                break;
            case R.id.btnCV:
                if(sharedPreferences.getBoolean(Config.IS_LOGGED, false)) {
                    Util.jumpActivity(this,CreateCVActivity.class);
                } else {
                    Util.jumpActivity(this,SignInActivity.class);
                }

                break;
            case R.id.btnMyJob:
                Util.jumpActivity(this, MyJobActivity.class);
                break;
            case R.id.btnSignOut:
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putBoolean(Config.IS_LOGGED, false);
                editor1.apply();
                firebaseAuth.signOut();
                btnAccount.setVisibility(View.GONE);
                btnSignIn.setVisibility(View.VISIBLE);
                break;
            case R.id.btnAccount:
                Intent intentAc = new Intent(this, ProfileUserActivity.class);
                startActivity(intentAc);
                break;
            case R.id.btnChangeUserType:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Config.USER_TYPE, 0);
                editor.putBoolean(Config.IS_LOGGED, false);
                editor.apply();
                firebaseAuth.signOut();
                Util.jumpActivity(this, SelectUserTypeActivity.class);
                break;
            default:
                break;
        }

    }

    void saveRecentSearch(Search search) {
        List<Search> searches = Paper.book().read("searches");
        if(searches == null) {
            searches = new ArrayList<>();
        }
        searches.add(0,search);
        if(searches.size() > 10) {
            searches.remove(9);
        }
        Paper.book().write("searches",searches);
        for (int i = 0;i < searches.size();i++) {
            Log.e("kiemtraSearch",searches.get(i).getTimkiem());
        }
    }

    void showRecentSearch() {
        List<Search> searches = Paper.book().read("searches");
        if(searches == null) return;
        if(searches.size() > 3) {
            List<Search> temp = new ArrayList<>();
            temp.add(searches.get(0));
            temp.add(searches.get(1));
            temp.add(searches.get(2));
            searches = temp;
        }
        RecenteSearchAdapter adapter = new RecenteSearchAdapter(this,searches);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }
}
