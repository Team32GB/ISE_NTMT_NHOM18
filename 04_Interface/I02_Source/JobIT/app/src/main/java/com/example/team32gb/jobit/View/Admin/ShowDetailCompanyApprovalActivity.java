package com.example.team32gb.jobit.View.Admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.AdminApproval.CompanyWaitingApprovalModel;
import com.example.team32gb.jobit.Model.SignUpAccountBusiness.InfoCompanyModel;
import com.example.team32gb.jobit.Presenter.AdminApproval.PresenterAdminApprovalRecruiter;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import static com.example.team32gb.jobit.Utility.Config.DATE_SEND_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_COMPANY_KEY;
import static com.example.team32gb.jobit.Utility.Config.REF_INFO_COMPANY;

public class ShowDetailCompanyApprovalActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtDate;
    private TextView txtType;
    private TextView txtSize;
    private TextView txtAddress;
    private TextView txtIntroduce;
    private TextView txtNamePresenter;
    private TextView txtPhoneNumberPresenter;
    private Button btnApprovalGreen;
    private Button btnApprovalCancelRed;
    private AppCompatImageButton imgAvatarCompanyApproval;
    private Toolbar toolbar;
    private ActionBar actionBar;
    Context context;
    private Dialog dialog;
    private Button btnApproval;
    private Button btnApprovalCancel;
    private TextView txtAdminAskBeforeApproval;
    private ProgressDialog progressDialog;

    PresenterAdminApprovalRecruiter presenter;
    DatabaseReference refData;
    InfoCompanyModel model = new InfoCompanyModel();
    CompanyWaitingApprovalModel modelCompanyWaiting;
    String idCompany;
    String dateSendApproval;
    boolean isApproval = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_copany_approval);
        context = getApplicationContext();

        txtName = findViewById(R.id.txtNameCompanyApproval);
        txtDate = findViewById(R.id.txtDateSendCompanyApproval);
        txtType = findViewById(R.id.txtTypeCompanyApproval);
        txtSize = findViewById(R.id.txtSizeCompanyApproval);
        txtAddress = findViewById(R.id.txtAddressCompanyApproval);
        txtIntroduce = findViewById(R.id.txtIntroduceCompanyApproval);
        txtNamePresenter = findViewById(R.id.txtNamePresentCompanyApproval);
        txtPhoneNumberPresenter = findViewById(R.id.txtPhoneCompanyApproval);
        btnApprovalGreen = findViewById(R.id.btnDetailCompanyApprovalOK);
        btnApprovalCancelRed = findViewById(R.id.btnDetailCompanyApprovalCancel);
        imgAvatarCompanyApproval = findViewById(R.id.imgAvatarCompanyApproval);

        toolbar = findViewById(R.id.tbDetailCompanyApprval);
        setSupportActionBar(toolbar);
        actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ShowDetailCompanyApprovalActivity.this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle!=null) {
            idCompany = bundle.getString(ID_COMPANY_KEY);
            dateSendApproval = bundle.getString(DATE_SEND_KEY);
            modelCompanyWaiting = new CompanyWaitingApprovalModel(idCompany, dateSendApproval);

            refData = FirebaseDatabase.getInstance().getReference().child(REF_INFO_COMPANY).child(idCompany);
            if (refData != null) {
                refData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        model = dataSnapshot.getValue(InfoCompanyModel.class);
                        Log.e("admin", "get model company");
                        txtName.setText(model.getName());
                        Log.e("admin", "Set text name company");
                        txtDate.setText("Ngày gửi yêu cầu: " + dateSendApproval);
                        txtType.setText("Kiểu công ty: " + model.getType());
                        txtSize.setText("Quy mô công ty: " + model.getSize());
                        txtAddress.setText("Địa chỉ: " + model.getAddress());
                        txtIntroduce.setText("Giới thiệu: " + model.getIntroduce());
                        txtNamePresenter.setText("Liên hệ: " + model.getNamePresenter());
                        txtPhoneNumberPresenter.setText("Số điện thoại: " + model.getPhoneNumberPresenter());

                        btnApprovalGreen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("admin", "test btn");
                                isApproval = true;

                                setUpDialog();
                                dialog.show();
                            }
                        });
                        btnApprovalCancelRed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("admin", "test btn");
                                isApproval = false;

                                setUpDialog();
                                dialog.show();
                            }
                        });
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else{
                Toast.makeText(this, "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        String avatarPath = Environment.getExternalStorageDirectory() + "/logo" +
//                "" + "/" + idCompany + ".jpg";
//        Log.e("kiemtraanh", avatarPath);
//        Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
//        if (bitmap != null && avatarPath != null && !avatarPath.isEmpty()) {
//            imgAvatarCompanyApproval.setBackground(new BitmapDrawable(bitmap));
//        } else {
//            long ONE_MEGABYTE = 1024 * 1024;
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Config.REF_FOLDER_LOGO).child(idCompany);
//            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    imgAvatarCompanyApproval.setImageBitmap(bitmap);
//                    Util.saveImageToLocal(bitmap, idCompany);
//                }
//            });
//        }
    }

    void setUpDialog() {
        presenter = new PresenterAdminApprovalRecruiter();
        dialog = new Dialog(ShowDetailCompanyApprovalActivity.this);

        View v = LayoutInflater.from(context).inflate(R.layout.admin_approval_dialog, null);
        btnApproval = v.findViewById(R.id.btnAdminApprovalOKDialog);
        btnApprovalCancel = v.findViewById(R.id.btnAdminApprovalCancelDialog);
        txtAdminAskBeforeApproval = v.findViewById(R.id.txtAdminAskBeforeApproval);


        if (isApproval) {
            txtAdminAskBeforeApproval.setText("Bạn có chắc chắn muốn duyệt hồ sơ này?");
        } else {
            txtAdminAskBeforeApproval.setText("Bạn có chắc chắn hủy hồ sơ này?");
        }

        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isApproval) {
                    if (presenter.onApproval(modelCompanyWaiting, isApproval)) {
                        dialog.dismiss();
                        Toast.makeText(ShowDetailCompanyApprovalActivity.this, "Duyệt thành công", Toast.LENGTH_SHORT).show();
                        closeActivity();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(ShowDetailCompanyApprovalActivity.this, "Duyệt thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {  //Không duyệt
                    if (presenter.onApproval(modelCompanyWaiting, isApproval)) {
                        dialog.dismiss();
                        Toast.makeText(ShowDetailCompanyApprovalActivity.this, "Hủy hồ sơ thành công", Toast.LENGTH_SHORT).show();
                        closeActivity();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(ShowDetailCompanyApprovalActivity.this, "Hủy hồ sơ thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnApprovalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(v);
    }

    void closeActivity() {
        Intent intent = new Intent(ShowDetailCompanyApprovalActivity.this, AdminApprovalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeAdmin:
                Intent intent = new Intent(ShowDetailCompanyApprovalActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
