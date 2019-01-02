package com.example.team32gb.jobit.View.SignUpAccountBusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.SignUpAccountBusiness.InfoCompanyModel;
import com.example.team32gb.jobit.Presenter.SignUpAccountBusiness.PresenterInSignUpAccountBusiness;
import com.example.team32gb.jobit.Presenter.SignUpAccountBusiness.PresenterLogicSignUpAccountBusiness;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SignUpAccountBusiness extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, ViewSignUpAccountBusiness {
    private Toolbar myToolBar;
    private ActionBar actionBar;
    private Spinner spType, spProvince;
    private AppCompatSpinner spSize;
    private Button btnSignUp;
    private AppCompatImageButton imageButton;
    private EditText edtName, edtAddress, edtIntroduce, edtNamePresenter, edtPhonePresenter;
    private boolean valid = false;
    private PresenterInSignUpAccountBusiness presenter;
    private InfoCompanyModel infoCompanyModel;
    private static final int SELECT_PICTURE = 10;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_account_business);

        myToolBar = findViewById(R.id.tbEditDetailCompany);

        myToolBar.setTitle("Thông tin công ty");
        setSupportActionBar(myToolBar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        spSize = findViewById(R.id.spSize);
        spType = findViewById(R.id.spType);
        spProvince = findViewById(R.id.spProvince);
        btnSignUp = findViewById(R.id.btnSignUp);

        edtName = findViewById(R.id.edtNameCompanySignUp);
        edtAddress = findViewById(R.id.edtAddressCompany);
        edtIntroduce = findViewById(R.id.edtDescriptionCompany);
        edtNamePresenter = findViewById(R.id.edtContact);
        edtPhonePresenter = findViewById(R.id.edtPhonePresenter);

        imageButton = findViewById(R.id.imgAvatarCompanyDetail);
        imageButton.setOnClickListener(this);
        uid = FirebaseAuth.getInstance().getUid();

        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this, R.array.QuyMoCongTy, R.layout.support_simple_spinner_dropdown_item);
        spSize.setAdapter(adapterSize);

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.DanhSachLoaiHinhCongTy, R.layout.support_simple_spinner_dropdown_item);
        spType.setAdapter(adapterType);

        ArrayAdapter<CharSequence> adapterProvince = ArrayAdapter.createFromResource(this, R.array.TinhThanh, R.layout.support_simple_spinner_dropdown_item);
        spProvince.setAdapter(adapterProvince);

        Intent intent = getIntent();
        infoCompanyModel = intent.getParcelableExtra("bundle");
        if (infoCompanyModel != null) {
            edtNamePresenter.setText(infoCompanyModel.getNamePresenter());
            edtName.setText(infoCompanyModel.getName());
            edtPhonePresenter.setText(infoCompanyModel.getPhoneNumberPresenter());
            edtAddress.setText(infoCompanyModel.getAddress());
            edtIntroduce.setText(infoCompanyModel.getIntroduce());

            Util.loadImageFromFolderLogoLocal(imageButton,uid);

            List<String> lsQuyMoCongTy = Arrays.asList(getResources().getStringArray(R.array.QuyMoCongTy));
            List<String> lsLoaiHinhCongTy = Arrays.asList(getResources().getStringArray(R.array.DanhSachLoaiHinhCongTy));
            List<String> lsTinhThanh = Arrays.asList(getResources().getStringArray(R.array.TinhThanh));

            spSize.setSelection(Util.getPositionSpinnerFromString(infoCompanyModel.getSize(), lsQuyMoCongTy));
            adapterSize.notifyDataSetChanged();

            spType.setSelection(Util.getPositionSpinnerFromString(infoCompanyModel.getType(), lsLoaiHinhCongTy));
            adapterType.notifyDataSetChanged();

            spProvince.setSelection(Util.getPositionSpinnerFromString(infoCompanyModel.getProvince(), lsTinhThanh));
            adapterProvince.notifyDataSetChanged();
        }

        spSize.setOnItemSelectedListener(this);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtName.getText().toString().length() <= 0) {
                    valid = false;
                    edtName.setError("Chưa nhập tên công ty");
                } else
                    valid = true;
            }
        });
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtAddress.getText().toString().length() <= 0) {
                    valid = false;
                    edtAddress.setError("Chưa nhập địa chỉ công ty");
                } else
                    valid = true;
            }
        });
        edtIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtIntroduce.getText().toString().length() <= 0) {
                    valid = false;
                    edtIntroduce.setError("Chưa nhập mô tả công ty");
                } else
                    valid = true;
            }
        });
        edtNamePresenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtNamePresenter.getText().toString().length() <= 0) {
                    valid = false;
                    edtNamePresenter.setError("Chưa nhập tên người liên hệ");
                } else
                    valid = true;
            }
        });
        edtPhonePresenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtPhonePresenter.getText().toString().length() <= 0) {
                    valid = false;
                    edtPhonePresenter.setError("Chưa nhập số điện thoại của người liên hệ");
                } else
                    valid = true;
            }
        });

        btnSignUp.setOnClickListener(this);

        presenter = new PresenterLogicSignUpAccountBusiness(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSignUp:
                if (edtName.getText().toString().length() <= 0) {
                    valid = false;
                    edtName.setError("Chưa nhập tên công ty");
                }
                if (edtAddress.getText().toString().length() <= 0) {
                    valid = false;
                    edtAddress.setError("Chưa nhập địa chỉ công ty");
                }
                if (edtIntroduce.getText().toString().length() <= 0) {
                    valid = false;
                    edtIntroduce.setError("Chưa nhập mô tả công ty");
                }
                if (edtNamePresenter.getText().toString().length() <= 0) {
                    valid = false;
                    edtNamePresenter.setError("Chưa nhập tên người liên hệ");
                }
                if (edtPhonePresenter.getText().toString().length() <= 0) {
                    valid = false;
                    edtPhonePresenter.setError("Chưa nhập số điện thoại của người liên hệ");
                }
                if (valid) {
                    Save();
                    Toast.makeText(getApplication(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Config.REGESTERED_INFO, true);
                    editor.apply();
                    Util.jumpActivityRemoveStack(this,HomeRecruitmentActivity.class);
                } else {
                    Toast.makeText(getApplication(), "Đăng kí không thành thành công", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imgAvatarCompanyDetail:
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Picture"), SELECT_PICTURE);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void showInfoCompany(InfoCompanyModel companyModel) {

    }

    public void Save() {
        InfoCompanyModel companyModel = new InfoCompanyModel();

        companyModel.setName(edtName.getText().toString());
        companyModel.setAddress(edtAddress.getText().toString());
        companyModel.setIntroduce(edtIntroduce.getText().toString());
        companyModel.setNamePresenter(edtNamePresenter.getText().toString());
        companyModel.setPhoneNumberPresenter(edtPhonePresenter.getText().toString());
        companyModel.setSize(spSize.getSelectedItem().toString());
        companyModel.setType(spType.getSelectedItem().toString());
        companyModel.setProvince(spProvince.getSelectedItem().toString());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();

        presenter.saveInfoCompany(uid, companyModel);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.listjob_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageURI = data.getData();
                try {
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageURI);
                    int heightBitmapThumbnail = (int) (100 * ((bitmap.getHeight() * 1.f) / bitmap.getWidth()));
                    final Bitmap bitmapThumbnail = ThumbnailUtils.extractThumbnail(bitmap, 100, heightBitmapThumbnail);

                    imageButton.setBackground(new BitmapDrawable(bitmapThumbnail));
                    Log.e("kiemtraImage", "1");
                    File folderDownloaded = new File(Environment.getExternalStorageDirectory() + "/logo");
                    if (!folderDownloaded.exists()) {
                        folderDownloaded.mkdir();
                    }
                    String avatarPath = Environment.getExternalStorageDirectory() + "/logo" + "/" + uid + ".jpg";
                    Log.e("kiemtraImage", avatarPath);
                    File file = new File(avatarPath);
                    FileOutputStream fileOutputStream;
                    fileOutputStream = new FileOutputStream(file);
                    Log.e("kiemtraImage", avatarPath);
                    bitmapThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                    fileOutputStream.flush();
                    fileOutputStream.close();

                    StorageReference srAvatar = FirebaseStorage.getInstance().getReference().child(Config.REF_FOLDER_LOGO);
                    final StorageReference storageReferenceImage = srAvatar.child(uid);
                    final DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data1 = baos.toByteArray();
                    UploadTask uploadTask = storageReferenceImage.putBytes(data1);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            nodeRoot.child("companys").child(uid).child("avatar").setValue(storageReferenceImage.getPath());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Picasso.get().load(selectedImageURI).into(imgAvatarProfile);

                //Lấy type của hình ảnh
//                ContentResolver contentResolver = this.getContentResolver();
//                MimeTypeMap mime = MimeTypeMap.getSingleton();
//                String type = mime.getExtensionFromMimeType(contentResolver.getType(selectedImageURI));
//                presenterJobSeekerProfile.saveImageProfile(selectedImageURI, "");
            }
        }
    }

}
