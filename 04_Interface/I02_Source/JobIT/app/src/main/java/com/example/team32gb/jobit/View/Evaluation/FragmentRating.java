package com.example.team32gb.jobit.View.Evaluation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.Report.ReportJobseekerModel;
import com.example.team32gb.jobit.Model.Report.ReportWaitingAdminApprovalModel;
import com.example.team32gb.jobit.Model.SignUpAccountBusiness.InfoCompanyModel;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.Admin.AdminReportModel;
import com.example.team32gb.jobit.View.CompanyDetail.CompanyDetailActivity;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;

public class FragmentRating extends Fragment implements View.OnClickListener {
    private Context context;
    private ListView lstRatingMem;
    private ListView lstComment;
    private AppCompatRatingBar rbAverage, rb1, rb2, rb3, rb4;
    private TextView tvstartAverage, tvstart1, tvstart2, tvstart3, tvstart4;
    private Button btnAddRating;
    private Dialog dialog;
    private String idCompany;
    private static RatingModel ratingModel = new RatingModel();
    private static float ratingAverage = 0.f, rating1 = 0.f, rating2 = 0.f, rating3 = 0.f, rating4 = 0.f;
    private float ratingTotalAverage = 0, ratingTotal1 = 0, ratingTotal2 = 0, ratingTotal3 = 0, ratingTotal4 = 0;
    private float ratingCompanyAverage = 0, ratingCompany1 = 0, ratingCompany2 = 0, ratingCompany3 = 0, ratingCompany4 = 0;
    private DatabaseReference nodeRating;
    private String uid;
    private int typeUser;
    private boolean isLogged;
    private ArrayList<RatingModel> ratingModels = new ArrayList<>();
    private ArrayList<String> listId = new ArrayList<>();
    private RatingModel myRating = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(context);
        // uid = FirebaseAuth.getInstance().getUid();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        typeUser = sharedPreferences.getInt(Config.USER_TYPE, 0);
        isLogged = sharedPreferences.getBoolean(Config.IS_LOGGED, false);
        if (typeUser == Config.IS_JOB_SEEKER && isLogged) {
            uid = FirebaseAuth.getInstance().getUid();
        }
        Bundle bundle = getArguments();
        idCompany = bundle.getString("idCompany");
        nodeRating = FirebaseDatabase.getInstance().getReference().child(Config.REF_RATING).child(idCompany);
        Log.e("kiemtraRating", idCompany);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rating, container, false);

        lstRatingMem = v.findViewById(R.id.lstRatingMem);
        btnAddRating = v.findViewById(R.id.btnAddReview);
        if (typeUser != Config.IS_JOB_SEEKER) {
            btnAddRating.setVisibility(View.GONE);
        }

        rbAverage = v.findViewById(R.id.rbAverage);
        rb1 = v.findViewById(R.id.rb1);
        rb2 = v.findViewById(R.id.rb2);
        rb3 = v.findViewById(R.id.rb3);
        rb4 = v.findViewById(R.id.rb4);

        tvstartAverage = v.findViewById(R.id.txtAverageRate);
        tvstart1 = v.findViewById(R.id.tvStar1);
        tvstart2 = v.findViewById(R.id.tvStar2);
        tvstart3 = v.findViewById(R.id.tvStar3);
        tvstart4 = v.findViewById(R.id.tvStar4);

        btnAddRating.setOnClickListener(this);

        lstComment = v.findViewById(R.id.lstComment);
        showRating();

        return v;
    }

    public void showRating() {
        nodeRating.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount();
                if (dataSnapshot.hasChildren()) {

                    RatingModel ratingModel;
                    for (DataSnapshot rating : dataSnapshot.getChildren()) {
                        if (Objects.equals(rating.getKey(), uid)) {
                            myRating = rating.getValue(RatingModel.class);
                            btnAddRating.setText("Sửa đánh giá");
                        } else {
                            ratingModel = rating.getValue(RatingModel.class);
                            sumRating(ratingModel);
                            ratingModels.add(ratingModel);
                            listId.add(rating.getKey());
                        }
                    }
                    if (myRating != null) {
                        ratingModels.add(0, myRating);
                        listId.add(0, uid);
                        sumRating(myRating);
                    }

                    ratingCompanyAverage = ratingTotalAverage / size;
                    ratingCompany1 = ratingTotal1 / size;
                    ratingCompany2 = ratingTotal2 / size;
                    ratingCompany3 = ratingTotal3 / size;
                    ratingCompany4 = ratingTotal4 / size;

                    ratingCompanyAverage = Math.round(ratingCompanyAverage * 10) / 10.0f;
                    ratingCompany1 = Math.round(ratingCompany1 * 10) / 10.0f;
                    ratingCompany2 = Math.round(ratingCompany2 * 10) / 10.0f;
                    ratingCompany3 = Math.round(ratingCompany3 * 10) / 10.0f;
                    ratingCompany4 = Math.round(ratingCompany4 * 10) / 10.0f;

                    rbAverage.setRating(ratingCompanyAverage);
                    rb1.setRating(ratingCompany1);
                    rb2.setRating(ratingCompany2);
                    rb3.setRating(ratingCompany3);
                    rb4.setRating(ratingCompany4);

                    tvstartAverage.setText(String.valueOf(ratingCompanyAverage));
                    tvstart1.setText(String.valueOf(ratingCompany1));
                    tvstart2.setText(String.valueOf(ratingCompany2));
                    tvstart3.setText(String.valueOf(ratingCompany3));
                    tvstart4.setText(String.valueOf(ratingCompany4));
                    //Log.e("kiemtraRating",ratingAverage + ":" + size + ":" + ratingTotalAverage);

                    CustomCommentAdapter customCommentAdapter = new CustomCommentAdapter(context, ratingModels,mCommunication);
                    lstComment.setAdapter(customCommentAdapter);
                    setListViewHeightBasedOnChildren(lstComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sumRating(RatingModel ratingModel) {
        ratingTotalAverage += ratingModel.getRatingAverage();
        ratingTotal1 += ratingModel.getRating1();
        ratingTotal2 += ratingModel.getRating2();
        ratingTotal3 += ratingModel.getRating3();
        ratingTotal4 += ratingModel.getRating4();
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAddReview:
                if (isLogged) {
                    setUpDialogReview();
                } else {
                    Util.jumpActivity(context, SignInActivity.class);
                }
                break;
            default:
                break;
        }
    }

    public void setUpDialogReview() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_diaglog_rating, null);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);

        final RatingBar rbAverage, rb1, rb2, rb3, rb4;
        final TextView tvAverage, tvrb1, tvrb2, tvrb3, tvrb4;
        final Button btnOk, btnCancel;
        final EditText edtComment, edtTitle;

        rbAverage = dialogView.findViewById(R.id.AverageRatingBar);
        rb1 = dialogView.findViewById(R.id.rb1);
        rb2 = dialogView.findViewById(R.id.rb2);
        rb3 = dialogView.findViewById(R.id.rb3);
        rb4 = dialogView.findViewById(R.id.rb4);
        tvAverage = dialogView.findViewById(R.id.txtAverageRate);
        tvrb1 = dialogView.findViewById(R.id.tvStar1);
        tvrb2 = dialogView.findViewById(R.id.tvStar2);
        tvrb3 = dialogView.findViewById(R.id.tvStar3);
        tvrb4 = dialogView.findViewById(R.id.tvStar4);
        btnOk = dialogView.findViewById(R.id.btnOk);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        edtComment = dialogView.findViewById(R.id.edtComment);
        edtTitle = dialogView.findViewById(R.id.edtTitle);
        final String uid = FirebaseAuth.getInstance().getUid();

        if (myRating != null) {
            ratingAverage = Float.parseFloat(Double.toString(myRating.ratingAverage));
            rating1 = Float.parseFloat(Double.toString(myRating.rating1));
            rating2 = Float.parseFloat(Double.toString(myRating.rating2));
            rating3 = Float.parseFloat(Double.toString(myRating.rating3));
            rating4 = Float.parseFloat(Double.toString(myRating.rating4));

            rbAverage.setRating(ratingAverage);
            rb1.setRating(rating1);
            rb2.setRating(rating2);
            rb3.setRating(rating3);
            rb4.setRating(rating4);

            tvAverage.setText(String.valueOf(ratingAverage));
            tvrb1.setText(String.valueOf(rating1));
            tvrb2.setText(String.valueOf(rating2));
            tvrb3.setText(String.valueOf(rating3));
            tvrb4.setText(String.valueOf(rating4));

            edtTitle.setText(myRating.title);
            edtComment.setText(myRating.comment);
        }

        assert uid != null;
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingModel.setComment(edtComment.getText().toString());
                ratingModel.setRatingAverage(Double.parseDouble(Float.toString(ratingAverage)));
                ratingModel.setRating1(Double.parseDouble(Float.toString(rating1)));
                ratingModel.setRating2(Double.parseDouble(Float.toString(rating2)));
                ratingModel.setRating3(Double.parseDouble(Float.toString(rating3)));
                ratingModel.setRating4(Double.parseDouble(Float.toString(rating4)));
                ratingModel.setDateTime(Util.getCurrentDay());
                ratingModel.setTitle(edtTitle.getText().toString());
                nodeRating.child(uid).setValue(ratingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rbAverage.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    tvAverage.setText(Float.toString(rating));
                    rb1.setRating(rating);
                    rb2.setRating(rating);
                    rb3.setRating(rating);
                    rb4.setRating(rating);
                    rating1 = rating2 = rating3 = rating4 = rating;
                    tvrb1.setText(Float.toString(rating));
                    tvrb2.setText(Float.toString(rating));
                    tvrb3.setText(Float.toString(rating));
                    tvrb4.setText(Float.toString(rating));
                }

            }
        });

        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating1 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4) / 4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb1.setText(Float.toString(rating));
            }
        });
        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating2 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4) / 4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb2.setText(Float.toString(rating));
            }
        });

        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating3 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4) / 4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb3.setText(Float.toString(rating));
            }
        });
        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating4 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4) / 4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb4.setText(Float.toString(rating));
            }
        });

        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(width, height);
        dialog.setTitle("Đánh giá nhà tuyển dụng");
        dialog.show();
    }
    private void setUpDialogReport(final int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_tocao, null);
        final EditText edtComment = dialogView.findViewById(R.id.edtComment);
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportJobseekerModel reportModel = new ReportJobseekerModel();
                reportModel.setDecription(edtComment.getText().toString());
                reportModel.setDateSendReport(Util.getCurrentDay());
                reportModel.setIdAccused(listId.get(position));
                reportModel.setIdReporter(uid);
                reportModel.setAdminComment("");
                reportModel.setisWarned(false);
                reportModel.setIdCommentInvalid(idCompany);


                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child(Config.REF_REPORT).child("jobseekers").child(reportModel.getIdAccused());
                String idReport = df.push().getKey();

                ReportWaitingAdminApprovalModel adminApprovalModel = new ReportWaitingAdminApprovalModel();
                adminApprovalModel.setDateSendReport(reportModel.getDateSendReport());
                adminApprovalModel.setIdAccused(reportModel.getIdAccused());
                adminApprovalModel.setIdReport(idReport);

                DatabaseReference dfAdminApproval = FirebaseDatabase.getInstance().getReference().child(Config.REF_REPORT_WAITING_ADMIN_APPROVAL).child("jobseekers").child(idReport);
                dfAdminApproval.setValue(adminApprovalModel);

                df = df.child(idReport);
                df.setValue(reportModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Tố cáo thành công", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setTitle("Tố cáo");
        dialog.show();
    }

    FragmentCommunication mCommunication = new FragmentCommunication() {
        @Override
        public void respond(int position) {
            Log.e("kiemtraTocao", position + ":" + listId.get(position));
            setUpDialogReport(position);
        }
    };
}