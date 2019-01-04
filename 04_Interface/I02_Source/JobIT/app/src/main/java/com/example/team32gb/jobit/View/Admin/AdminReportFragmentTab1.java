package com.example.team32gb.jobit.View.Admin;


import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.team32gb.jobit.Utility.Config.DATE_SEND_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_ACCUSED_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_REPORT_KEY;
import static com.example.team32gb.jobit.Utility.Config.IS_ACTIVE;
import static com.example.team32gb.jobit.Utility.Config.REF_JOBSEEKERS_NODE;
import static com.example.team32gb.jobit.Utility.Config.REF_REPORT_WAITING_ADMIN_APPROVAL;


/**
 * A simple {@link Fragment} subclass.
 * TAB1: Employee's report
 */
public class AdminReportFragmentTab1 extends Fragment {
    private RecyclerView rvReportEmployee;
    List<AdminReportModel> arrReport = new ArrayList<>();
    DatabaseReference refData;
    private FirebaseRecyclerAdapter<ReportWaitingAdminApprovalModel, AdminListReportViewHolder> adaptor;
    private String nameAccused;
    private String timeLeftSendReport;
    private ReportWaitingAdminApprovalModel modelReportWaiting;
    private PresenterAdminApprovalReport presenter;
    private ProgressDialog progressDialog;
    //private  final boolean[] isActive = {true};   //!SAI


    public AdminReportFragmentTab1() {
        // Required empty public constructor
        presenter = new PresenterAdminApprovalReport();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_report_tab1_, container, false);
        rvReportEmployee = v.findViewById(R.id.rvReportEmployee);

        refData = FirebaseDatabase.getInstance().getReference().child(REF_REPORT_WAITING_ADMIN_APPROVAL).child(REF_JOBSEEKERS_NODE);
        FirebaseRecyclerOptions<ReportWaitingAdminApprovalModel> options = new FirebaseRecyclerOptions.Builder<ReportWaitingAdminApprovalModel>()
                .setQuery(refData, ReportWaitingAdminApprovalModel.class).build();

        adaptor = new FirebaseRecyclerAdapter<ReportWaitingAdminApprovalModel, AdminListReportViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminListReportViewHolder holder, int position, @NonNull final ReportWaitingAdminApprovalModel model) {
                /*Lấy tên người bị tố cáo*/
                DatabaseReference refNameAccused = FirebaseDatabase.getInstance().getReference().
                        child(REF_JOBSEEKERS_NODE).child(model.getIdAccused()).child("name");

                refNameAccused.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nameAccused = (String) dataSnapshot.getValue();
                        timeLeftSendReport = Util.getSubTime(model.getDateSendReport());
                        holder.txtName.setText(nameAccused);
                        holder.txtDateSendReport.setText(timeLeftSendReport);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public AdminListReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_report_row_item, viewGroup, false);
                final AdminListReportViewHolder viewHolder = new AdminListReportViewHolder(v);

                //Chọn 1 item trong list
                viewHolder.SetOnClickListener(new AdminListReportViewHolder.ClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        modelReportWaiting = getItem(viewHolder.getAdapterPosition());
                        //kiểm tra xem tài khoản user có còn active hay không
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                                child(REF_JOBSEEKERS_NODE).child(modelReportWaiting.getIdAccused()).child(IS_ACTIVE);

                       ref.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               boolean isActive = (boolean) dataSnapshot.getValue();
                               if (isActive==true) {
                                   //Nhảy qua activity show detail report
                                   Intent intent = new Intent(getActivity(), AdminShowDetailReportJobseekerActivity.class);
                                   Bundle bundle = new Bundle();
                                   bundle.putString(ID_REPORT_KEY, modelReportWaiting.getIdReport());
                                   bundle.putString(DATE_SEND_KEY, modelReportWaiting.getDateSendReport());
                                   bundle.putString(ID_ACCUSED_KEY, modelReportWaiting.getIdAccused());
                                   intent.putExtra("bundle", bundle);
                                   startActivity(intent);
                               }
                               else{
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
                        modelReportWaiting = getItem(viewHolder.getAdapterPosition());
                        //kiểm tra xem tài khoản user có còn active hay không
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                                child(REF_JOBSEEKERS_NODE).child(modelReportWaiting.getIdAccused()).child(IS_ACTIVE);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean isActive = (boolean) dataSnapshot.getValue();
                                if (isActive==true) {
                                    setUpDialogSendWarning(modelReportWaiting);
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
                        modelReportWaiting = getItem(viewHolder.getAdapterPosition());
                        setupDialogIgnoreReport(modelReportWaiting);
                    }
                });
                return viewHolder;
            }


        };

        adaptor.startListening();

        rvReportEmployee.setAdapter(adaptor);
        rvReportEmployee.setLayoutManager(new LinearLayoutManager(getActivity()));
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_recyclerview_admin);
        rvReportEmployee.setLayoutAnimation(animationController);
        runLayoutAnimation(rvReportEmployee);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adaptor.stopListening();
    }


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_recyclerview_admin);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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
                if (message.equals("")) {
                    edtMessageFromAdmin.setError("Bạn phải nhập nhắc nhở cảnh báo");
                } else {
                    presenter.onSendWarningReportToJobseeker(model, message);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void setupDialogIgnoreReport(final ReportWaitingAdminApprovalModel model) {
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
                presenter.onIgnoreReportJobseeker(model);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
