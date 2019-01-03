package com.example.team32gb.jobit.View.CompanyDetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.example.team32gb.jobit.CustomView.WrapcontentViewPager;
import com.example.team32gb.jobit.Model.SignUpAccountBusiness.InfoCompanyModel;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.Admin.AdminHomeActivity;
import com.example.team32gb.jobit.View.Admin.AdminReportAdapterFragment;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.SignUpAccountBusiness.SignUpAccountBusiness;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CompanyDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tlReport;
    private ViewPager vpReport;
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private AppCompatImageButton imageButton;
    private TextView txtNameCompany, txtSizeCompany, txtTypeCompany, txtAddress, txtIntroduce, txtContact, txtPhone;
    private Button btnEdit;
    private LinearLayout lnEdit;
    private ProgressDialog progressDialog;
    private String idCompany;
    private int userType;
    private OnAboutDataReceivedListener mAboutDataListener;

    public interface OnAboutDataReceivedListener {
        void onDataReceived(InfoCompanyModel model);
        void onDataRatingReceived(String id);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        myToolBar = findViewById(R.id.tbDetailCompany);

        imageButton = findViewById(R.id.imgAvatarCompanyDetail);

        txtNameCompany = this.findViewById(R.id.txtNameCompany);
        txtAddress = this.findViewById(R.id.txtAddressCompany);
        txtContact = this.findViewById(R.id.txtContact);
        txtPhone = this.findViewById(R.id.txtPhone);
        txtSizeCompany = this.findViewById(R.id.txtSizeCompanyDetail);
        txtTypeCompany = this.findViewById(R.id.txtTypeCompanyDetail);

        lnEdit = this.findViewById(R.id.lnEdit);
        btnEdit = this.findViewById(R.id.btnEditJob);

        txtIntroduce = this.findViewById(R.id.txtIntroduceCompany);

        tlReport = findViewById(R.id.tlReport);
        vpReport = findViewById(R.id.vpReport);



        myToolBar.setTitle("Thông tin công ty");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageButton.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userType = sharedPreferences.getInt(Config.USER_TYPE, 0);
        if (userType == Config.IS_RECRUITER) {
            idCompany = FirebaseAuth.getInstance().getUid();
            lnEdit.setVisibility(View.VISIBLE);
        } else {
            final Intent intent = getIntent();
            idCompany = intent.getStringExtra("idCompany");
        }
        //mAboutDataListener.onDataRatingReceived(idCompany);
        DetailCompanyAdapterFragment adapter = new DetailCompanyAdapterFragment(this, getSupportFragmentManager(),idCompany);
        vpReport.setAdapter(adapter);
        tlReport.setupWithViewPager(vpReport);

        String avatarPath = Environment.getExternalStorageDirectory() + "/logo" +
                "" + "/" + idCompany + ".jpg";
        Log.e("kiemtraanh", avatarPath);
        Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
        if (bitmap != null && avatarPath != null && !avatarPath.isEmpty()) {
            imageButton.setBackground(new BitmapDrawable(bitmap));
        } else {
            long ONE_MEGABYTE = 1024 * 1024;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Config.REF_FOLDER_LOGO).child(idCompany);
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageButton.setImageBitmap(bitmap);
                    Util.saveImageToLocal(bitmap, idCompany);
                }
            });
        }


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
                if(userType == Config.IS_JOB_SEEKER) {
                    Util.jumpActivityRemoveStack(this,HomeJobSeekerActivity.class);
                } else if(userType == Config.IS_RECRUITER) {
                    Util.jumpActivityRemoveStack(this,HomeRecruitmentActivity.class);
                } else {
                    Util.jumpActivityRemoveStack(this,AdminHomeActivity.class);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.imgAvatarCompanyDetail:
                break;
        }
    }

    private void showInfo() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("companys").child(idCompany);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final InfoCompanyModel model = dataSnapshot.getValue(InfoCompanyModel.class);
                if (model != null) {
                    mAboutDataListener.onDataReceived(model);

                    txtNameCompany.setText(model.getName());
                    txtAddress.setText("Địa chỉ: " + model.getAddress());
                    txtContact.setText("Liên hệ: " + model.getNamePresenter());
                    txtPhone.setText("Số điện thoại: " + model.getPhoneNumberPresenter());
//                    txtSizeCompany.setText("Quy mô công ty: " + model.getSize());
//                    txtTypeCompany.setText("Kiểu công ty: " + model.getType());
//                    txtIntroduce.setText(model.getIntroduce());

                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(CompanyDetailActivity.this, SignUpAccountBusiness.class);
                            intent1.putExtra("bundle", model);
                            startActivity(intent1);
                        }
                    });
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(CompanyDetailActivity.this, "Lỗi khi lấy thông tin", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
