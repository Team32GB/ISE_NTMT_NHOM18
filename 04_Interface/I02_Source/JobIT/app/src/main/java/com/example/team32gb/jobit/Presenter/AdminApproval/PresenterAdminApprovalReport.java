package com.example.team32gb.jobit.Presenter.AdminApproval;

import com.example.team32gb.jobit.Lib.EventBus;
import com.example.team32gb.jobit.Model.Report.ReportWaitingAdminApprovalModel;
import com.example.team32gb.jobit.Model.UnActiveUser.UnactiveUserModel;

public class PresenterAdminApprovalReport implements PresenterInAdminApprovalReport {
    private EventBus eventBus;

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void onSendWarningReportToJobseeker(ReportWaitingAdminApprovalModel model, String messageFromAdmin) {
        model.onSendWarningReportToJobseeker(messageFromAdmin);
    }

    @Override
    public void onIgnoreReportJobseeker(ReportWaitingAdminApprovalModel model) {
        model.onIgnoreReportJobseeker();
    }

    @Override
    public void onSendWarningReportToRecruiter(ReportWaitingAdminApprovalModel model, String messageFromAdmin) {
        model.onSendWarningReportToRecuiter(messageFromAdmin);
    }

    @Override
    public void onIgnoreReportRecruiter(ReportWaitingAdminApprovalModel model) {
        model.onIgnoreReportRecuiter();
    }

}
