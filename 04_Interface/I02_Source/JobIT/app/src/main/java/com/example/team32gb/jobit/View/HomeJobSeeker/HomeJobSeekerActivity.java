package com.example.team32gb.jobit.View.HomeJobSeeker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.CreateCV.CreateCVActivity;
import com.example.team32gb.jobit.View.ProfileUser.ProfileUserActivity;
import com.example.team32gb.jobit.View.ListJobSearch.ListJobSearchActivity;
import com.example.team32gb.jobit.View.MyJob.MyJobActivity;
import com.example.team32gb.jobit.View.SelectUserType.SelectUserTypeActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeJobSeekerActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSearch;
    private Button btnTimViec;
    private Button btnSignIn;
    private Button btnCreateCV;
    private Button btnMyJob;
    private Button btnAccount;
    private Button btnSignOut;
    private Button btnChangeUserType;
    private AppCompatAutoCompleteTextView edtTimKiem, edtDiaDiem;
    private SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        edtDiaDiem = findViewById(R.id.edtDiaDiem);

        btnSearch = findViewById(R.id.btnSearch);
        btnSignIn = findViewById(R.id.btnDangNhap);
        btnCreateCV = findViewById(R.id.btnCV);
        btnTimViec = findViewById(R.id.btnSearch);
        btnMyJob = findViewById(R.id.btnMyJob);
        btnAccount = findViewById(R.id.btnAccount);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnChangeUserType = findViewById(R.id.btnChangeUserType);

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
        btnTimViec.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnCreateCV.setOnClickListener(this);
        btnMyJob.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        btnChangeUserType.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSearch:
                Intent intent = new Intent(this,ListJobSearchActivity.class);
                Bundle bundle = new Bundle();
                String timKiem = edtTimKiem.getText().toString();
                String diaDiem  = edtDiaDiem.getText().toString();
                if(timKiem.trim().length() <= 0 && diaDiem.trim().length() <= 0) {
                    edtTimKiem.setError("Hãy nhập thông tin tìm kiếm");
                    edtDiaDiem.setError("Hãy nhập thông tin tìm kiếm");

                } else {
                    bundle.putString("timKiem",edtTimKiem.getText().toString());
                    bundle.putString("diaDiem",edtDiaDiem.getText().toString());
                    intent.putExtra("bundle",bundle);
                    this.startActivity(intent);
                }

                break;
            case R.id.btnDangNhap:
                Intent intentSI = new Intent(this, SignInActivity.class);
                startActivity(intentSI);
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
                this.finish();
                break;
            default:
                break;
        }

    }
}
