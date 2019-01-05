package com.example.team32gb.jobit.View.Admin;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.Report.ReportWaitingAdminApprovalModel;
import com.example.team32gb.jobit.Presenter.AdminApproval.PresenterAdminApprovalReport;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.team32gb.jobit.Utility.Config.DATE_SEND_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_ACCUSED_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_REPORT_KEY;
import static com.example.team32gb.jobit.Utility.Config.REF_INFO_COMPANY;
import static com.example.team32gb.jobit.Utility.Config.REF_JOBSEEKERS_NODE;
import static com.example.team32gb.jobit.Utility.Config.REF_RECRUITERS_NODE;
import static com.example.team32gb.jobit.Utility.Config.REF_REPORT_WAITING_ADMIN_APPROVAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminReportFragmentTab2 extends Fragment {
    private RecyclerView rvEmployer;
    private FirebaseRecyclerAdapter<ReportWaitingAdminApprovalModel, AdminListReportViewHolder> adapter;
    private String nameCompany;
    private String dateSend;
    private ReportWaitingAdminApprovalModel reportModel;
    private PresenterAdminApprovalReport presenter;


    public AdminReportFragmentTab2() {
        // Required empty public constructor
        reportModel = new ReportWaitingAdminApprovalModel();
        presenter = new PresenterAdminApprovalReport();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.admin_report_tab2, container, false);
        rvEmployer = v.findViewById(R.id.rvReportEmployer);

        DatabaseReference refReportWaiting = FirebaseDatabase.getInstance().getReference()
                .child(REF_REPORT_WAITING_ADMIN_APPROVAL).child(REF_RECRUITERS_NODE);

        FirebaseRecyclerOptions <ReportWaitingAdminApprovalModel> options = new FirebaseRecyclerOptions.Builder<ReportWaitingAdminApprovalModel>()
                .setQuery(refReportWaiting, ReportWaitingAdminApprovalModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<ReportWaitingAdminApprovalModel, AdminListReportViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminListReportViewHolder holder, int position, @NonNull final ReportWaitingAdminApprovalModel model) {
                //Lấy tên công ty bị tố cáo
                DatabaseReference refNameCompany = FirebaseDatabase.getInstance().getReference()
                        .child(REF_INFO_COMPANY).child(model.getIdAccused()).child("name");

                refNameCompany.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nameCompany = (String)dataSnapshot.getValue();
                        holder.txtName.setText(nameCompany);
                        dateSend = Util.getSubTime(model.getDateSendReport());
                        holder.txtDateSendReport.setText(dateSend);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public AdminListReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_report_row_item, null);
                final AdminListReportViewHolder viewHolder = new AdminListReportViewHolder(v);

                viewHolder.SetOnClickListener(new AdminListReportViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        reportModel = getItem(viewHolder.getAdapterPosition());
                        //kiểm tra tài khoản này có tồn tại hay không
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                                child(REF_RECRUITERS_NODE).child(reportModel.getIdAccused()).child("isActive");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean isActive = (boolean) dataSnapshot.getValue();

                                if (isActive==true) {
                                    //todo: jum Activity
                                    //Nhảy qua activity show detail report
                                    Intent intent = new Intent(getActivity(), AdminShowDetailReportRecruiterActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(ID_REPORT_KEY, reportModel.getIdReport());
                                    bundle.putString(DATE_SEND_KEY, reportModel.getDateSendReport());
                                    bundle.putString(ID_ACCUSED_KEY, reportModel.getIdAccused());
                                    intent.putExtra("bundle", bundle);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getActivity(), "Tài khoản user này đã bị khóa, không thể xem chi tiết", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.ibtnSendWarningReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reportModel = getItem(viewHolder.getAdapterPosition());
                        //kiểm tra xem tài khoản user có còn active hay không
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                                child(REF_RECRUITERS_NODE).child(reportModel.getIdAccused()).child("isActive");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean isActive = (boolean) dataSnapshot.getValue();
                                if (isActive){
                                    setUpDialogSendWarning(reportModel );
                                }
                                else{
                                    Toast.makeText(getActivity(), "Tài khoản user này đã bị khóa, không thể gửi cảnh cáo", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                viewHolder.ibtnIgnoreReportJobSeeker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reportModel = getItem(viewHolder.getAdapterPosition());
                        setupDialogIgnoreReport( reportModel);
                    }
                });
                return viewHolder;
            }
        };

        adapter.startListening();
        rvEmployer.setAdapter(adapter);
        rvEmployer.setLayoutManager(new LinearLayoutManager(getActivity()));

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_recyclerview_admin);
        rvEmployer.setLayoutAnimation(animationController);
        runLayoutAnimation(rvEmployer);
        return v;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_recyclerview_admin);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    public void setUpDialogSendWarning(final ReportWaitingAdminApprovalModel model) {
        final EditText edtMessageFromAdmin;
        Button btnSenWarningOK;
        Button btnSenWarningCancel;

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.admin_report_dialog_send_warning, null);
        edtMessageFromAdmin = dialogView.findViewById(R.id.edtMessageFromAdmin);
        btnSenWarningOK = dialogView.findViewById(R.id.btnSenWarningOK);
        btnSenWarningCancel = dialogView.findViewById(R.id.btnSenWarningCancel);


        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogView);
        dialog.setTitle("Gửi cảnh cáo");

        btnSenWarningCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSenWarningOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessageFromAdmin.getText().toString();
                if (message.equals("")){
                    edtMessageFromAdmin.setError("Bạn phải nhập nhắc nhở cảnh báo");
                }
                else{
                    presenter.onSendWarningReportToRecruiter(model,message);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void setupDialogIgnoreReport( final ReportWaitingAdminApprovalModel model){
        Button btnIgnoreReportCancel;
        Button btnIgnoreReportOK;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.admin_report_dialog_ignore, null);
        btnIgnoreReportOK = view.findViewById(R.id.btnIgnoreReportOK);
        btnIgnoreReportCancel = view.findViewById(R.id.btnIgnoreReportCancel);

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        dialog.setTitle("Bỏ qua tố cáo");

        btnIgnoreReportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnIgnoreReportOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: presenter ...
                presenter.onIgnoreReportRecruiter(model);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
