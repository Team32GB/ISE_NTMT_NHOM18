package com.example.team32gb.jobit.View.HomeRecruitmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.team32gb.jobit.JobRecruitmentActivity;
import com.example.team32gb.jobit.ListCandidateAcvitity;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.View.CompanyDetail.CompanyDetailActivity;
import com.example.team32gb.jobit.View.ProfileUser.ProfileUserActivity;
import com.example.team32gb.jobit.View.SelectUserType.SelectUserTypeActivity;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.PostJob.PostJobRecruitmentActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeRecruitmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUpLoad,btnPost,btnFileOfRecruit, btnSignOurRecruit, btnChangeUserType, btnProfileAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_recruit);

        btnUpLoad = findViewById(R.id.btnUploadPost);
        btnPost = findViewById(R.id.btnPost);
        btnFileOfRecruit = findViewById(R.id.btnFileOfRecruit);
        btnProfileAccount = findViewById(R.id.btnProfileAccount);
        btnSignOurRecruit = findViewById(R.id.btnSignOutRecruit);
        btnChangeUserType = findViewById(R.id.btnChangeUserTypeRecruiter);

        btnUpLoad.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnFileOfRecruit.setOnClickListener(this);
        btnProfileAccount.setOnClickListener(this);
        btnChangeUserType.setOnClickListener(this);
        btnSignOurRecruit.setOnClickListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Config.USER_TYPE,Config.IS_RECRUITER);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btnUploadPost:
                Util.jumpActivity(this,PostJobRecruitmentActivity.class);
                // class đăng tin do nguyên làm cái này, chèn vô activity vô đây
                // intent = new Intent(this,JobRecruitmentActivity.class);
              //  startActivity(intent);
                break;
            case R.id.btnPost:
                 Util.jumpActivity(this,JobRecruitmentActivity.class);
               // startActivity(intent);
                break;
            case R.id.btnFileOfRecruit:
                 Util.jumpActivity(this,CompanyDetailActivity.class);
                break;
            case R.id.btnProfileAccount:
                Util.jumpActivity(this,ProfileUserActivity.class);
                break;
            case R.id.btnChangeUserTypeRecruiter:
                SharedPreferences sharedPreferences1 = getSharedPreferences(Config.SHARED_PREFERENCES_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor1 =sharedPreferences1.edit();
                editor1.putInt(Config.USER_TYPE,0);
                editor1.apply();
                Util.jumpActivity(this,SelectUserTypeActivity.class);
                break;
            case R.id.btnSignOutRecruit:
                Util.signOut(FirebaseAuth.getInstance(),this);
                Util.jumpActivityRemoveStack(HomeRecruitmentActivity.this,SignInActivity.class);
                break;
            default:
                return;

        }
    }
}
