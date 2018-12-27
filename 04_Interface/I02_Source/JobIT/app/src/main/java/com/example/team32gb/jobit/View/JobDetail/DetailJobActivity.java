package com.example.team32gb.jobit.View.JobDetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.CompanyDetail.CompanyDetailActivity;
import com.example.team32gb.jobit.View.CreateCV.CreateCVActivity;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.PostJob.PostJobRecruitmentActivity;
import com.example.team32gb.jobit.View.PostedJob.DetailPostedJobActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailJobActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private TextView txtNameJob, txtTime, txtSalary, txtTypeJob, txtNumberOfCadidates, txtJobDescription, txtJobRequierment;
    private Button btnSave, btnApply;
    private String idJob, idCompany;
    private Button btnEdit;
    private AppCompatImageButton btnAvatar;
    private ProgressDialog progressDialog;
    private LinearLayout lnApply, lnEdit;
    private DatabaseReference nodeRoot;
    private ItemPostJob itemPostJob;
    private boolean isLogged;
    private int typeUser;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailjob);

        myToolBar = findViewById(R.id.tbDetailCompany);
        btnApply = findViewById(R.id.btnApply);
        btnEdit = this.findViewById(R.id.btnEditJob);


        btnApply.setOnClickListener(this);

        myToolBar = findViewById(R.id.tbDetailCompany);
        myToolBar.setTitle("Chi tiết");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nodeRoot = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        lnApply = findViewById(R.id.lnApply);
        lnEdit = findViewById(R.id.lnEdit);
        uid = FirebaseAuth.getInstance().getUid();

        final SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        isLogged = sharedPreferences.getBoolean(Config.IS_LOGGED, false);
        if (!sharedPreferences.getBoolean(Config.IS_ACTIVITY_APPLY, false)) {
            lnApply.setVisibility(View.GONE);
        }
        typeUser = sharedPreferences.getInt(Config.USER_TYPE, 0);
        if (typeUser == Config.IS_RECRUITER) {
            lnEdit.setVisibility(View.VISIBLE);
            lnApply.setVisibility(View.INVISIBLE);
        } else {
            lnEdit.setVisibility(View.GONE);
        }

        txtNameJob = findViewById(R.id.txtNameJobDetail);
        txtTime = findViewById(R.id.txtTime);
        txtSalary = findViewById(R.id.txtSalaryDetail);
        txtTypeJob = findViewById(R.id.txtTypeJob);
        txtNumberOfCadidates = findViewById(R.id.txtNumberOfCandidates);
        txtJobDescription = findViewById(R.id.txtJobDescription);
        txtJobRequierment = findViewById(R.id.txtJobRequirements);

        btnSave = findViewById(R.id.btnSaveJob);
        btnApply = findViewById(R.id.btnApply);
        btnAvatar = findViewById(R.id.imgAvatarCompany);

        btnSave.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        btnAvatar.setOnClickListener(this);



        final Intent intent = getIntent();
        itemPostJob = intent.getParcelableExtra("bundle");
        idCompany = itemPostJob.getDataPostJob().getIdCompany();
        idJob = itemPostJob.getDataPostJob().getIdJob();



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailJobActivity.this, PostJobRecruitmentActivity.class);
                intent1.putExtra("detail", itemPostJob.getDataPostJob());
                intent1.putExtra("idJob", idJob);
                startActivity(intent1);
            }
        });
        if (isLogged && typeUser == Config.IS_JOB_SEEKER) {
                    if (itemPostJob.isApplied()) {
                        btnApply.setText("Applied");
                        btnApply.setEnabled(false);
                    }
                    if (itemPostJob.isChecked()) {
                        btnSave.setText("Đã Lưu");
                        btnSave.setEnabled(false);
                    }
                }
        progressDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showInfo();
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
                if (typeUser == Config.IS_JOB_SEEKER) {
                    Util.jumpActivityRemoveStack(this, HomeJobSeekerActivity.class);
                } else {
                    Util.jumpActivityRemoveStack(this, HomeRecruitmentActivity.class);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnApply:
                if (isLogged) {
                    final String idJobseeker = FirebaseAuth.getInstance().getUid();
                    nodeRoot.child("cvs").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(idJobseeker)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                                String currentDate = sdf.format(new Date());

                                DatabaseReference dfTimeApplied = nodeRoot.child("choDuyets").child(idCompany).child(idJob).child(idJobseeker).child("timeApplied");
                                dfTimeApplied.setValue(currentDate);

                                DatabaseReference dfDaApply = nodeRoot.child("Applieds").child(idJobseeker).child(idCompany).child(idJob).child("timeAppiled");
                                dfDaApply.setValue(currentDate);
                                Toast.makeText(DetailJobActivity.this, "Apply thành công", Toast.LENGTH_SHORT).show();

                                btnApply.setEnabled(false);
                                btnApply.setText("Applied");
                            } else {
                                Util.jumpActivity(DetailJobActivity.this, CreateCVActivity.class);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    Util.jumpActivity(DetailJobActivity.this, SignInActivity.class);
                }
                break;
            case R.id.btnSaveJob:
                SharedPreferences sharedPreferencesSave = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                if (sharedPreferencesSave.getBoolean(Config.IS_LOGGED, false)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    String currentDate = sdf.format(new Date());
                    DatabaseReference dfDaLuus = nodeRoot.child("daLuus").child(uid).child(idCompany).child(idJob).child("timeSaved");
                    dfDaLuus.setValue(currentDate).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailJobActivity.this, "Lưu tin thất bại", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailJobActivity.this, "Lưu tin thành công", Toast.LENGTH_LONG).show();
                            btnSave.setEnabled(false);
                            btnSave.setText("Đã Lưu");
                        }
                    });
                } else {
                    Util.jumpActivity(DetailJobActivity.this, SignInActivity.class);
                }
                break;
            case R.id.imgAvatarCompany:
                Intent intent = new Intent(this, CompanyDetailActivity.class);
                intent.putExtra("idCompany", idCompany);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    private void showInfo() {
        String avatarPath = Environment.getExternalStorageDirectory() + "/logo" + "/" + idCompany + ".jpg";
        Log.e("kiemtraanh",avatarPath);
        Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
        if(bitmap != null && avatarPath != null && !avatarPath.isEmpty()) {
            btnAvatar.setBackground(new BitmapDrawable(bitmap));
        }
        txtNameJob.setText(itemPostJob.getDataPostJob().getNameJob());
        txtSalary.setText("Từ $" + itemPostJob.getDataPostJob().getMinSalary() + " đến $" + itemPostJob.getDataPostJob().getMaxSalary() + "/" + itemPostJob.getDataPostJob().getEach());
        txtTypeJob.setText(itemPostJob.getDataPostJob().getTypeJob());
        txtNumberOfCadidates.setText(itemPostJob.getDataPostJob().getNumberEmployer());
        txtJobDescription.setText(itemPostJob.getDataPostJob().getDescription());
        txtJobRequierment.setText(itemPostJob.getDataPostJob().getQualification());
        txtTime.setText(Util.getSubTime(itemPostJob.getDataPostJob().getTime()));
    }
}
