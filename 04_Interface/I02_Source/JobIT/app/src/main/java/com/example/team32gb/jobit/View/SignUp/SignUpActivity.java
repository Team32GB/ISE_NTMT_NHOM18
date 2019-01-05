package com.example.team32gb.jobit.View.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.JobSeekerProfile.UserModel;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeJobSeeker.HomeJobSeekerActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.example.team32gb.jobit.View.SignUpAccountBusiness.SignUpAccountBusiness;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnSignUp;
    private TextView tvJumpSignIn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmailSignUp);
        edtPassword = findViewById(R.id.edtPasswordSignUp);
        edtConfirmPassword = findViewById(R.id.edtConfirmPasswordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvJumpSignIn = findViewById(R.id.tvJumpSignIn);

        progressDialog = new ProgressDialog(this);

        btnSignUp.setOnClickListener(this);
        tvJumpSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSignUp:
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                userType = sharedPreferences.getInt(Config.USER_TYPE, 0);
                Log.e("kiemtrasignup",userType+"");
                switch (userType) {
                    case Config.IS_JOB_SEEKER:
                        signUp(Config.REF_JOBSEEKERS_NODE);
                        break;
                    case Config.IS_RECRUITER:
                        signUp(Config.REF_RECRUITERS_NODE);
                        break;
                    case Config.IS_ADMIN:
                        signUp(Config.REF_ADMINS_NODE);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.tvJumpSignIn:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private boolean checkInfoInput() {
        boolean isvalid = true;
        if (edtName.getText().toString().trim().length() <= 0) {
            edtName.requestFocus();
            isvalid = false;
            edtName.setError("Hãy nhập tên");
        }
        if (edtEmail.getText().toString().trim().length() <= 0) {
            isvalid = false;
            edtEmail.requestFocus();
            edtEmail.setError("Hãy nhập email");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString().trim()).matches()) {
            isvalid = false;
            edtEmail.requestFocus();
            edtEmail.setError("Nhập sai định dạng email");
        }
        if (edtPassword.getText().toString().trim().length() <= 0) {
            isvalid = false;
            edtPassword.requestFocus();
            edtPassword.setError("Hãy nhập mật khẩu");
        }
        if (edtPassword.getText().toString().trim().length() < 6) {
            isvalid = false;
            edtPassword.requestFocus();
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
        }
        if (edtConfirmPassword.getText().toString().trim().length() <= 0) {
            isvalid = false;
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.setError("Hãy xác nhận mật khẩu");
        }
        if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {
            isvalid = false;
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.setError("2 mật khẩu không trùng nhau");
        }
        return isvalid;
    }

    private void saveInfoToLocal() {
        //Lưu thông tin vào bộ nhớ máy
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.NAME_USER, edtName.getText().toString());
        editor.putString(Config.EMAIL_USER, edtEmail.getText().toString());
        editor.putString(Config.PASSWORD_USER, edtPassword.getText().toString());
        editor.putBoolean(Config.SIGN_UP_WITH_EMAIL, true);
        editor.putBoolean(Config.IS_LOGGED,true);
        editor.apply();
    }

    private void saveInfotToServer(String refUserType, String uid) {
        //Lưu thông tin lên server
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(refUserType).child(uid);
        UserModel userModel = new UserModel();
        userModel.setEmail(edtEmail.getText().toString());
        userModel.setName(edtName.getText().toString());
        databaseReference.setValue(userModel);
    }

    private void handlingProgressBar() {
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void signUp(final String refUserType) {
        if (checkInfoInput()) {
            handlingProgressBar();
            Log.e("kiemtrasignup",refUserType);

            final String email = edtEmail.getText().toString();
            final String password = edtPassword.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                        saveInfoToLocal();

                        String uid = task.getResult().getUser().getUid();
                        saveInfotToServer(refUserType, uid);

                        switch (userType) {
                            case Config.IS_JOB_SEEKER:
                                SignUpActivity.this.finish();
                                Util.jumpActivity(SignUpActivity.this, HomeJobSeekerActivity.class);
                                break;
                            case Config.IS_RECRUITER:
                                SignUpActivity.this.finish();
                                Util.jumpActivity(SignUpActivity.this, SignUpAccountBusiness.class);
                                break;
                            case Config.IS_ADMIN:
                                //signUp(Config.REF_ADMINS_NODE);
                                break;
                            default:
                                break;
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại, vui lòng đăng ký bằng Email khác", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
