package com.example.team32gb.jobit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener {
    private Button btnLogin, btnLoginGoogle, btnLoginFacebook, btnCreateAccount;
    private EditText edtEmail, edtPassword;
    private TextView tvForgotPassword;
    private ProgressDialog progressDialog;

    private GoogleApiClient apiClient;
    private FirebaseAuth firebaseAuth;
    CallbackManager callbackManager;
    public static int REQUEST_CODE_LOGIN_GOOGLE = 3;
    public static int REQUEST_CODE_LOGIN_FACEBOOK = 3;

    public static int provider = -1;
    public static int LOGIN_WITH_GMAIL = 0;
    public static int LOGIN_WITH_GOOGLE = 1;
    public static int LOGIN_WITH_FACEBOOK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_sign_in);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.bgBlackTransparent40));

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        edtEmail = findViewById(R.id.edtEmailLogIn);
        edtPassword = findViewById(R.id.edtpasswordLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();

        btnLoginGoogle.setOnClickListener(this);
        btnLoginFacebook.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        CreateClientGoogle();
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnLogin:
                LoginWithEmail();
                break;
            case R.id.btnLoginGoogle:
                LoginWithGoogle();
                break;
            case R.id.btnLoginFacebook:
                LoginWithFacebook();
                break;
            case R.id.btnCreateAccount:
                Intent intentCA = new Intent(this, SignUpActivity.class);
                startActivity(intentCA);
                break;
            case R.id.tvForgotPassword:
                Intent intentFG = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentFG);
                break;
            default:
                break;
        }
    }

    public void LoginWithEmail() {
        if(checkInfoInput()) {
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean checkInfoInput() {
        boolean isValid = true;
        if(edtEmail.getText().toString().trim().length() <= 0) {
            edtEmail.requestFocus();
            isValid = false;
            edtEmail.setError("Hãy nhập email");
        }
        if(edtPassword.getText().toString().trim().length() <= 0) {
            edtPassword.requestFocus();
            isValid = false;
            edtPassword.setError("Hãy nhập mật khẩu");
        }
        return isValid;
    }
    public void LoginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        String tokenID = loginResult.getAccessToken().getToken();
                        authenticateGoogle(tokenID);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void CreateClientGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void LoginWithGoogle() {
        Intent iGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        provider = LOGIN_WITH_GOOGLE;
        startActivityForResult(iGoogle, REQUEST_CODE_LOGIN_GOOGLE);
    }

    private void authenticateGoogle(String tokenID) {
        if (provider == LOGIN_WITH_GOOGLE) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenID, null);
            firebaseAuth.signInWithCredential(authCredential);
        } else {
            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
            firebaseAuth.signInWithCredential(authCredential);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = signInResult.getSignInAccount();
                String tokenID = account.getIdToken();
                authenticateGoogle(tokenID);
            }
        } else {
            //callbackManager.onActivityResult(requestCode,resultCode,data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, HomePageActivity.class);
//                startActivity(intent);
        } else {

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
