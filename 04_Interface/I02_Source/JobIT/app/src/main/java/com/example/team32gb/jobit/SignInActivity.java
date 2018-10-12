package com.example.team32gb.jobit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
public class SignInActivity extends AppCompatActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener {
        private Button btnLogin, btnLoginGoogle, btnLoginFacebook;
        private EditText edtEmail, edtPassword;
        private GoogleApiClient apiClient;
        private FirebaseAuth firebaseAuth;
        private LoginButton btnLoginFb;
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
            window.setStatusBarColor(this.getResources().getColor(R.color.bgRedTransparent90));

            btnLogin = findViewById(R.id.btnLogin);
            btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
            btnLoginFacebook = findViewById(R.id.btnLoginFacebook);

            edtEmail = findViewById(R.id.edtEmailLogIn);
            edtPassword = findViewById(R.id.edtpasswordLogin);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
//            LoginManager.getInstance().logOut();
            callbackManager = CallbackManager.Factory.create();

            btnLoginGoogle.setOnClickListener(this);
            btnLoginFacebook.setOnClickListener(this);
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.example.team32gb.jobit",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
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
                case R.id.btnLoginGoogle:
                    LoginWithGoogle();
                    break;
                case R.id.btnLogin:
                    break;
                case R.id.btnLoginFacebook:
                    fbLogin();
                    break;
                default:
                    break;
            }
        }

        public void fbLogin() {

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
                Toast.makeText(this, "Thanh Cong", Toast.LENGTH_SHORT).show();
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
