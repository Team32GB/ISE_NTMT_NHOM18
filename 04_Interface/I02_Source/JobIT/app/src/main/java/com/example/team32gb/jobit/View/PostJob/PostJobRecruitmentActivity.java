package com.example.team32gb.jobit.View.PostJob;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.team32gb.jobit.JobRecruitmentActivity;
import com.example.team32gb.jobit.Model.PostJob.DataPostJob;
import com.example.team32gb.jobit.Presenter.PostJob.PresenterInPostJob;
import com.example.team32gb.jobit.Presenter.PostJob.PresenterPostJob;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.HomeRecruitmentActivity.HomeRecruitmentActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PostJobRecruitmentActivity extends AppCompatActivity implements ViewPostJob,AdapterView.OnItemSelectedListener {
    private Toolbar myToolBar;
    private ActionBar actionBar;

    private EditText edtJobTitle;
    private Spinner spnJobType;
    private EditText edtMinSalary;
    private EditText edtMaxSalary;
    private Spinner spnEach;
    private Spinner spnNumHires;
    private EditText edtJobDescription;
    private EditText edtQualification;
    private Button btnPost;

    boolean valid = false;

    final ArrayList<String> Type = new ArrayList<String>();
    final ArrayList<String>  Each = new ArrayList<String>();
    final ArrayList<String> NumHires = new ArrayList<String>();


    private PresenterInPostJob presenter;
    private DataPostJob dataPostJob,dataPostJobEdit;

    private boolean isEdit;
    private String idJobEdit = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job_recruitment);

        myToolBar = findViewById(R.id.tbJobRecruitment);
        myToolBar.setTitle("Danh sách việc làm ");
        setSupportActionBar(myToolBar);

        edtJobTitle=findViewById(R.id.edtJobTittle);
        spnJobType=findViewById(R.id.spnJobType);
        edtMinSalary=findViewById(R.id.edtMinSalary);
        edtMaxSalary=findViewById(R.id.edtMaxSalary);
        spnEach=findViewById(R.id.spnEach);
        spnNumHires=findViewById(R.id.spnNumHires);
        edtJobDescription =findViewById(R.id.edtJobDescription);
        edtQualification=findViewById(R.id.edtQualification);
        btnPost=findViewById(R.id.btnPost);

        Intent intent = getIntent();
        dataPostJobEdit = intent.getParcelableExtra("detail");
        if(dataPostJobEdit != null) {
            idJobEdit = dataPostJobEdit.getIdJob();
        }


//        if(idJobEdit != null) {
//            Log.e("kiemtraid",idJobEdit + dataPostJobEdit);
//
//        } else {
//            Log.e("kiemtraid","fail");
//
//        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //xử lys spinner loại công việc
        Type.add("Bán thời gian");
        Type.add("Toàn thời gian");
        Type.add("Hợp đồng");
        Type.add("Thực tập");
        Type.add("Tạm thời");

        ArrayAdapter<String> adapterType=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Type);
        adapterType.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnJobType.setAdapter(adapterType);

        spnJobType.setOnItemSelectedListener(this);

        //Xử lý Spinner "Mỗi"
        Each.add("Tháng");
        Each.add("Giờ");
        Each.add("Ngày");
        Each.add("Tuần");
        Each.add("Năm");


        ArrayAdapter<String> adapterEach=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Each);
        adapterEach.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnEach.setAdapter(adapterEach);

        spnEach.setOnItemSelectedListener(this);

        //Xử lý Spinner số lượng người cần tuyển
        NumHires.add("1 người");
        NumHires.add("2 đến 5 người");
        NumHires.add("5 đến 10 người");
        NumHires.add("Hơn 10 người");
        NumHires.add("Nhu cầu liên tục");

        ArrayAdapter<String> adapterNumHires= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,NumHires);
        adapterNumHires.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnNumHires.setAdapter(adapterNumHires);

        spnNumHires.setOnItemSelectedListener(this);

        if(dataPostJobEdit != null) {
            isEdit = true;
            edtJobTitle.setText(dataPostJobEdit.getNameJob());
            edtMinSalary.setText(dataPostJobEdit.getMinSalary());
            edtMaxSalary.setText(dataPostJobEdit.getMaxSalary());
            edtJobDescription.setText(dataPostJobEdit.getDescription());
            edtQualification.setText(dataPostJobEdit.getQualification());

            spnJobType.setSelection(Util.getPositionSpinnerFromString(dataPostJobEdit.getTypeJob(),Type));
            adapterType.notifyDataSetChanged();

            spnEach.setSelection(Util.getPositionSpinnerFromString(dataPostJobEdit.getEach(),Each));
            adapterEach.notifyDataSetChanged();

            spnNumHires.setSelection(Util.getPositionSpinnerFromString(dataPostJobEdit.getNumberEmployer(),NumHires));
            adapterNumHires.notifyDataSetChanged();
        }

        edtJobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtJobTitle.getText().toString().length() <= 0)
                {
                    valid = false;
                    edtJobTitle.setError("Chưa nhập tên công việc");
                }
                else
                    valid = true;
            }
        });

        edtMinSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (edtMinSalary.getText().toString().length() <= 0) {

                        valid=false;
                        edtMinSalary.setError("Lương không hợp lệ");
                    }
                    else if(Integer.parseInt(edtMinSalary.getText().toString()) < 100){
                        valid=false;
                        edtMinSalary.setError("Lương phải lớn hơn $100");
                    }
                    else
                        valid = true;
            }
        });

        edtMaxSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtMaxSalary.getText().toString().length() <= 0) {

                    valid=false;
                    edtMaxSalary.setError("Lương không hợp lệ");

                }
                else  if(Integer.parseInt(edtMaxSalary.getText().toString()) < 100){
                    valid=false;
                    edtMaxSalary.setError("Lương lớn hơn $100");
                }
            }
        });

        edtJobDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtJobDescription.getText().toString().length() <= 0)
                {
                    valid = false;
                    edtJobDescription.setError("Chưa nhập mô tả công việc");
                }
                else
                    valid = true;
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtJobTitle.getText().toString().length() <= 0)
                {
                    valid = false;
                    edtJobTitle.setError("Chưa nhập tên công việc");
                }
                if (edtMinSalary.getText().toString().length() <= 0) {

                    valid=false;
                    edtMinSalary.setError("Lương không hợp lệ");
                }
                else  if(Integer.parseInt(edtMinSalary.getText().toString()) < 100){
                    valid=false;
                    edtMinSalary.setError("Lương phải lớn hơn $100");
                }
                if (edtMaxSalary.getText().toString().length() <= 0) {

                    valid=false;
                    edtMaxSalary.setError("Lương không hợp lệ");
                }
                else  if(Integer.parseInt(edtMaxSalary.getText().toString()) < 100){
                    valid=false;
                    edtMaxSalary.setError("Lương phải lớn hơn $100");
                }
                if (edtMinSalary.getText().toString().length() > 0 && edtMaxSalary.getText().toString().length() > 0){
                    if (Integer.parseInt(edtMinSalary.getText().toString()) >= Integer.parseInt(edtMaxSalary.getText().toString())) {
                        edtMaxSalary.setError("Lương phải lớn hơn");
                        valid = false;
                    }
                    else
                        valid = true;
                }
                if (edtJobDescription.getText().toString().length() <= 0) {
                    edtJobDescription.setError("Chưa nhập mô tả công việc");
                    valid = false;
                }
                if(valid)
                {
                    Save();
                    Toast.makeText(getApplication(), "Đăng kí thành công",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplication(), "Đăng kí không thành thành công",Toast.LENGTH_LONG).show();

            }
        });
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
                Util.jumpActivityRemoveStack(this,HomeRecruitmentActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    public void Save() {
        dataPostJob = new DataPostJob();

        dataPostJob.setNameJob(edtJobTitle.getText().toString());
        dataPostJob.setTypeJob(Type.get(spnJobType.getSelectedItemPosition()));
        dataPostJob.setMaxSalary(edtMaxSalary.getText().toString());
        dataPostJob.setMinSalary(edtMinSalary.getText().toString());
        dataPostJob.setEach(Each.get(spnEach.getSelectedItemPosition()));
        dataPostJob.setNumberEmployer(NumHires.get(spnNumHires.getSelectedItemPosition()));
        dataPostJob.setDescription(edtJobDescription.getText().toString());
        dataPostJob.setQualification(edtQualification.getText().toString());

        if(!isEdit) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String date = df.format(c.getTime());
            dataPostJob.setTime(date);
        } else {
            dataPostJob.setTime(dataPostJobEdit.getTime());
        }


        presenter = new PresenterPostJob(this);
        String uid = FirebaseAuth.getInstance().getUid();
        Log.e("kiemtraDetail",uid + ":" + idJobEdit);
        if(isEdit) {
            presenter.SavePostEdit(idJobEdit,uid,dataPostJob);
        } else {
            presenter.SavePost(uid,dataPostJob);
        }

        Log.e("kt","ad");


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getPositionSpinnerFromString(String string, ArrayList<String> ls) {
        for (int i = 0; i < ls.size();i++) {
            if(ls.get(i).equals(string)) {
                return i;
            }
        }
        return 0;
    }
 }
