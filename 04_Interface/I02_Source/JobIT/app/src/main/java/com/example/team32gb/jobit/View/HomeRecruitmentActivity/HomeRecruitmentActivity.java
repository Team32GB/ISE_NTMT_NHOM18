package com.example.team32gb.jobit.View.HomeRecruitmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.team32gb.jobit.Utility.Config.IS_ACTIVE;
import static com.example.team32gb.jobit.Utility.Config.REF_RECRUITERS_NODE;

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
                //kiểm tra xem account này có bị khóa hay không trước khi đăng tin tuyển dụng mới
                String idRecruiter = FirebaseAuth.getInstance().getUid();
                Log.e("kiem tra id", idRecruiter);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(REF_RECRUITERS_NODE).child(idRecruiter).child(IS_ACTIVE);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isRecruiterActive = (boolean)dataSnapshot.getValue();

                        if (isRecruiterActive){
                            Util.jumpActivity(getBaseContext(),PostJobRecruitmentActivity.class);
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Tài khoản của bạn đã bị khóa, không thể đăng tin tuyển dụng mới", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
