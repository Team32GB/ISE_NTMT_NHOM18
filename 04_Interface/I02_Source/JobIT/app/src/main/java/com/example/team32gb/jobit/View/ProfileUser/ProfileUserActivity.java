package com.example.team32gb.jobit.View.ProfileUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.JobSeekerProfile.UserModel;
import com.example.team32gb.jobit.Presenter.JobSeekerProfile.PresenterInJobSeekerProfile;
import com.example.team32gb.jobit.Presenter.JobSeekerProfile.PresenterLogicJobSeekerProfile;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.Admin.AdminHomeActivity;
import com.example.team32gb.jobit.View.ChangePassword.ChangePasswordActivity;
import com.example.team32gb.jobit.View.CreateCV.CreateCVActivity;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity implements View.OnClickListener, ViewProfileUser {
    private static final int SELECT_PICTURE = 10;
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private Button btnAuthenticateAccount, btnMyCV, btnChangePassword, btnSignOut;
    private EditText edtNameProfile;
    private ImageButton btnEditName, btnSaveNameProfile;
    private CircleImageView imgAvatarProfile;
    private TextView tvNameProfile, tvAuthentication, tvEmailProfile;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private SharedPreferences sharedPreferences;
    private String uid;
    private PresenterInJobSeekerProfile presenterJobSeekerProfile;
    private ProgressBar progressBar;
    private int typeUser;
    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_profile);

        btnAuthenticateAccount = findViewById(R.id.btnAuthenticateAccount);
        btnMyCV = findViewById(R.id.btnMyCV);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnSignOut = findViewById(R.id.btnSignOutProfile);
        btnEditName = findViewById(R.id.btnEditName);
        btnSaveNameProfile = findViewById(R.id.btnSaveNameProfile);
        imgAvatarProfile = findViewById(R.id.imgAvatarProfile);
        tvNameProfile = findViewById(R.id.tvNameProfile);
        tvAuthentication = findViewById(R.id.tvAuthentication);
        tvEmailProfile = findViewById(R.id.tvEmailProfile);
        edtNameProfile = findViewById(R.id.edtNameProfile);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fbUser != null;
        if (!fbUser.isEmailVerified()) {
            btnAuthenticateAccount.setVisibility(View.VISIBLE);
            tvAuthentication.setVisibility(View.VISIBLE);
        }
        btnAuthenticateAccount.setOnClickListener(this);
        btnMyCV.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnEditName.setOnClickListener(this);
        btnSaveNameProfile.setOnClickListener(this);
        imgAvatarProfile.setOnClickListener(this);

        myToolBar = findViewById(R.id.tbProfile);

        myToolBar.setTitle("Thông tin tài khoản");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        presenterJobSeekerProfile = new PresenterLogicJobSeekerProfile(this, uid);
        presenterJobSeekerProfile.onCreate();

        typeUser = sharedPreferences.getInt(Config.USER_TYPE, 0);
        if (typeUser == Config.IS_RECRUITER || typeUser == Config.IS_ADMIN) {
            btnMyCV.setVisibility(View.GONE);
        }
        if (!sharedPreferences.getBoolean(Config.MAY_GET_LOCAL, false)) {
            switch (typeUser) {
                case Config.IS_JOB_SEEKER:
                    presenterJobSeekerProfile.getProfile(Config.REF_JOBSEEKERS_NODE, uid);
                    break;
                case Config.IS_RECRUITER:
                    presenterJobSeekerProfile.getProfile(Config.REF_RECRUITERS_NODE, uid);
                    break;
                case Config.IS_ADMIN:
                    Log.e("kiemtraadmin", "admin");
                    presenterJobSeekerProfile.getProfile(Config.REF_ADMINS_NODE, uid);
                    break;
                default:
                    break;
            }

        } else {
            UserModel userModel = new UserModel();
            userModel.setName(sharedPreferences.getString(Config.NAME_USER, ""));
            userModel.setEmail(sharedPreferences.getString(Config.EMAIL_USER, ""));
            userModel.setAvatar(storageReference.child(Config.REF_FOLDER_AVATAR).child(uid).getPath());

            String avatarPath = Environment.getExternalStorageDirectory() + "/avatar" + "/" + uid + ".jpg";
            Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
            showProfile(userModel, bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterJobSeekerProfile.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnMyCV:
                Intent intentcv = new Intent(ProfileUserActivity.this, CreateCVActivity.class);
                startActivity(intentcv);
                break;
            case R.id.btnChangePassword:
                Intent intent = new Intent(ProfileUserActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSignOutProfile:
                firebaseAuth.signOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.IS_LOGGED, false);
                editor.apply();
                switch (typeUser) {
                    case Config.IS_JOB_SEEKER:
                        Util.jumpActivity(this, HomeJobSeekerActivity.class);
                        break;
                    case Config.IS_RECRUITER:
                        Util.jumpActivity(this, SignInActivity.class);
                        break;
                    case Config.IS_ADMIN:
                        Util.jumpActivity(this, SignInActivity.class);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btnEditName:
                btnEditName.setVisibility(View.GONE);
                tvNameProfile.setVisibility(View.GONE);
                edtNameProfile.setVisibility(View.VISIBLE);
                btnSaveNameProfile.setVisibility(View.VISIBLE);
                edtNameProfile.setText(tvNameProfile.getText().toString());
                break;
            case R.id.btnSaveNameProfile:
                btnEditName.setVisibility(View.VISIBLE);
                tvNameProfile.setVisibility(View.VISIBLE);
                edtNameProfile.setVisibility(View.GONE);
                btnSaveNameProfile.setVisibility(View.GONE);
                String name = edtNameProfile.getText().toString();
                tvNameProfile.setText(name);
                switch (typeUser) {
                    case Config.IS_JOB_SEEKER:
                        presenterJobSeekerProfile.saveNameProfile(Config.REF_JOBSEEKERS_NODE, uid, name);
                        break;
                    case Config.IS_RECRUITER:
                        presenterJobSeekerProfile.saveNameProfile(Config.REF_RECRUITERS_NODE, uid, name);
                        break;
                    case Config.IS_ADMIN:
                        presenterJobSeekerProfile.saveNameProfile(Config.REF_ADMINS_NODE, uid, name);
                        break;
                    default:
                        break;
                }
                Log.e("kiemtraSave", name);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString(Config.NAME_USER, name);
                editor1.apply();
                break;
            case R.id.imgAvatarProfile:
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Picture"), SELECT_PICTURE);
                break;
            case R.id.btnAuthenticateAccount:
                user.sendEmailVerification()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileUserActivity.this,
                                            "Đang gửi tin nhắc xác thực tới " + user.getEmail(),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ProfileUserActivity.this,
                                            "Lỗi khi gửi",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageURI);
                    int heightBitmapThumbnail = (int) (100 * ((bitmap.getHeight() * 1.f) / bitmap.getWidth()));
                    Bitmap bitmapThumbnail = ThumbnailUtils.extractThumbnail(bitmap, 100, heightBitmapThumbnail);

                    imgAvatarProfile.setImageBitmap(bitmapThumbnail);
                    Log.e("kiemtraImage", "1");
                    File folderDownloaded = new File(Environment.getExternalStorageDirectory() + "/avatar");
                    if (!folderDownloaded.exists()) {
                        folderDownloaded.mkdir();
                    }
                    String avatarPath = Environment.getExternalStorageDirectory() + "/avatar" + "/" + uid + ".jpg";
                    Log.e("kiemtraImage", avatarPath);
                    File file = new File(avatarPath);
                    FileOutputStream fileOutputStream;
                    fileOutputStream = new FileOutputStream(file);
                    imgAvatarProfile.setImageBitmap(bitmapThumbnail);
                    Log.e("kiemtraImage", avatarPath);
                    bitmapThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                    fileOutputStream.flush();
                    fileOutputStream.close();

                    switch (typeUser) {
                        case Config.IS_JOB_SEEKER:
                            presenterJobSeekerProfile.saveImageProfile(Config.REF_JOBSEEKERS_NODE, uid, bitmapThumbnail);
                            break;
                        case Config.IS_RECRUITER:
                            presenterJobSeekerProfile.saveImageProfile(Config.REF_RECRUITERS_NODE, uid, bitmapThumbnail);
                            break;
                        case Config.IS_ADMIN:
                            presenterJobSeekerProfile.saveImageProfile(Config.REF_ADMINS_NODE, uid, bitmapThumbnail);
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void showProfile(UserModel userModel, Bitmap bitmap) {
        Log.e("kiemtrashow", "show");
        if (!userModel.getAvatar().equals("") && bitmap != null) {
            Log.e("kiemtrashow", "a");
            imgAvatarProfile.setImageBitmap(bitmap);
        }
        tvEmailProfile.setText(userModel.getEmail());
        if (!userModel.getName().equals("")) {
            tvNameProfile.setText(userModel.getName());
        } else {
            tvNameProfile.setVisibility(View.GONE);
            btnEditName.setVisibility(View.GONE);
            edtNameProfile.requestFocus();
            edtNameProfile.setVisibility(View.VISIBLE);
            btnSaveNameProfile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProfile(UserModel userModel, Uri uri) {
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
            case R.id.tbHome:
                if (typeUser == Config.IS_JOB_SEEKER) {
                    Util.jumpActivityRemoveStack(this, HomeJobSeekerActivity.class);
                } else if (typeUser == Config.IS_RECRUITER) {
                    Util.jumpActivityRemoveStack(this, HomeRecruitmentActivity.class);
                } else {
                    Util.jumpActivityRemoveStack(this, AdminHomeActivity.class);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
