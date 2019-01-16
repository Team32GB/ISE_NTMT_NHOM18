package com.example.team32gb.jobit.View.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.ProfileUser.ProfileUserActivity;
import com.example.team32gb.jobit.View.SelectUserType.SelectUserTypeActivity;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnReport;
    Button btnApproval;
    Button btnStatistic;
    Button btnAccount;
    Button btnChangeUserType;
    Button btnUnactiveUser;
    Button btnFeedbackToAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnReport = findViewById(R.id.btnReport);
        btnApproval = findViewById(R.id.btnApproval);
        btnStatistic = findViewById(R.id.btnStatistic);
        btnAccount = findViewById(R.id.btnAccount);
        btnChangeUserType = findViewById(R.id.btnChangeUserType);
        btnUnactiveUser = findViewById(R.id.btnUnactiveUser);
        btnFeedbackToAdmin = findViewById(R.id.btnFeedbackToAdmin);

        btnReport.setOnClickListener(this);
        btnApproval.setOnClickListener(this);
        btnStatistic.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        btnChangeUserType.setOnClickListener(this);
        btnUnactiveUser.setOnClickListener(this);
        btnFeedbackToAdmin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnReport:
                intent = new Intent();
                intent.setClass(this, AdminReportActivity.class);
                startActivity(intent);
                break;
            case R.id.btnApproval:
                intent = new Intent();
                intent.setClass(this, AdminApprovalActivity.class);
                startActivity(intent);
                break;
            case R.id.btnStatistic:
                intent = new Intent();
                intent.setClass(this, AdminStatisticActivity.class);
                startActivity(intent);
                break;
            case R.id.btnChangeUserType:
                SharedPreferences.Editor editor = getSharedPreferences(Config.SHARED_PREFERENCES_NAME,MODE_PRIVATE).edit();
                editor.putInt(Config.USER_TYPE,0);
                editor.apply();
                Util.jumpActivity(this,SelectUserTypeActivity.class);
                break;
            case R.id.btnAccount:
                Util.jumpActivity(this,ProfileUserActivity.class);
                break;
            case R.id.btnUnactiveUser:
                intent = new Intent();
                intent.setClass(this, AdminUnactiveUserActivity.class);
                Log.e("kiemtra nhay acti", "Nhay qua unactive user");
                startActivity(intent);
                break;
            case R.id.btnFeedbackToAdmin:
                intent = new Intent();
                intent.setClass(this, AdminFeedback.class);
                Log.e("kiemtra nhay acti", "Nhay qua feedbacks user");
                startActivity(intent);
                break;
            default:
                break;


        }

    }
}
