package com.example.team32gb.jobit.View.SelectUserType;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team32gb.jobit.Lib.MyFirebaseMessagingService;
import com.example.team32gb.jobit.Model.Report.ReportJobseekerModel;
import com.example.team32gb.jobit.Model.Report.ReportWaitingAdminApprovalModel;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.Admin.AdminHomeActivity;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.example.team32gb.jobit.View.SignUpAccountBusiness.SignUpAccountBusiness;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.example.team32gb.jobit.Utility.Config.IS_LOGGED;
import static com.example.team32gb.jobit.Utility.Config.IS_RECRUITER;
import static com.example.team32gb.jobit.Utility.Config.REGESTERED_INFO;
import static com.example.team32gb.jobit.Utility.Config.SHARED_PREFERENCES_NAME;
import static com.example.team32gb.jobit.Utility.Config.USER_TYPE;

public class SelectUserTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnJobSeeker, btnEmployer, btnAdmin, btnDismiss;
    private SharedPreferences sharedPreferencesUserType;
    private int userType = 0;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        dialog = new Dialog(this);
        if (!Util.isConnectingToInternet(this)) {
            setUpDialogReport();
        } else {
            btnJobSeeker = findViewById(R.id.btnJobSeeker);
            btnEmployer = findViewById(R.id.btnEmployer);
            btnAdmin = findViewById(R.id.btnAdmin);
            btnDismiss = findViewById(R.id.btnDismiss);

            btnJobSeeker.setOnClickListener(this);
            btnEmployer.setOnClickListener(this);
            btnAdmin.setOnClickListener(this);
            btnDismiss.setOnClickListener(this);
            sharedPreferencesUserType = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            userType = sharedPreferencesUserType.getInt(USER_TYPE, 0);

            switch (userType) {
                case Config.IS_JOB_SEEKER:
                    Util.jumpActivityRemoveStack(this, HomeJobSeekerActivity.class);
                    break;
                case Config.IS_RECRUITER:
                    if (sharedPreferencesUserType.getBoolean(IS_LOGGED, false)) {
                        if (sharedPreferencesUserType.getBoolean(REGESTERED_INFO, false)) {
                            Util.jumpActivityRemoveStack(SelectUserTypeActivity.this, HomeRecruitmentActivity.class);
                        } else {
                            Util.jumpActivityRemoveStack(SelectUserTypeActivity.this, SignUpAccountBusiness.class);
                        }
                    } else {
                        Util.jumpActivityRemoveStack(SelectUserTypeActivity.this, SignInActivity.class);
                    }
                    break;
                case Config.IS_ADMIN:
                    if (sharedPreferencesUserType.getBoolean(IS_LOGGED, false)) {
                        Log.e("kiemtrajump", "true");
                        Util.jumpActivityRemoveStack(this, AdminHomeActivity.class);
                    } else {
                        Log.e("kiemtrajump", "false");

                        Util.jumpActivityRemoveStack(this, SignInActivity.class);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setUpDialogReport() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.lost_internet, null);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.jumpActivity(SelectUserTypeActivity.this, SelectUserTypeActivity.class);
            }
        });
        dialog.setContentView(dialogView);
        dialog.setTitle("Mất kết nối internet");
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        SharedPreferences.Editor editor = sharedPreferencesUserType.edit();
        switch (id) {
            case R.id.btnJobSeeker:
            case R.id.btnDismiss:
                firebaseAuth.signOut();
                editor.putBoolean(Config.IS_LOGGED, false);
                editor.putBoolean(Config.REGESTERED_INFO, false);
                editor.putInt(USER_TYPE, Config.IS_JOB_SEEKER);
                editor.apply();
                Util.jumpActivityRemoveStack(SelectUserTypeActivity.this, HomeJobSeekerActivity.class);
                break;
            case R.id.btnEmployer:
                firebaseAuth.signOut();
                editor.putBoolean(Config.IS_LOGGED, false);
                editor.putBoolean(Config.REGESTERED_INFO, false);
                editor.putInt(USER_TYPE, IS_RECRUITER);
                editor.apply();
                Util.jumpActivityRemoveStack(SelectUserTypeActivity.this, SignInActivity.class);
                this.finish();
                break;
            case R.id.btnAdmin:
                firebaseAuth.signOut();
                editor.putBoolean(Config.IS_LOGGED, false);
                editor.putBoolean(Config.REGESTERED_INFO, false);
                editor.putInt(USER_TYPE, Config.IS_ADMIN);
                editor.apply();
                Util.jumpActivityRemoveStack(SelectUserTypeActivity.this, SignInActivity.class);
                break;
            default:
                break;
        }
    }
}
