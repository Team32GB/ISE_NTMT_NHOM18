package com.example.team32gb.jobit.Presenter.AdminApproval;

import com.example.team32gb.jobit.Model.Report.ReportWaitingAdminApprovalModel;
import com.example.team32gb.jobit.Model.UnActiveUser.UnactiveUserModel;

interface PresenterInAdminApprovalReport {
    void onCreate();
    void onDestroy();
    void onSendWarningReportToJobseeker(ReportWaitingAdminApprovalModel model, String messageFromAdmin);
    void onIgnoreReportJobseeker(ReportWaitingAdminApprovalModel model);

    void onSendWarningReportToRecruiter(ReportWaitingAdminApprovalModel model, String messageFromAdmin);
    void onIgnoreReportRecruiter(ReportWaitingAdminApprovalModel model);
}
