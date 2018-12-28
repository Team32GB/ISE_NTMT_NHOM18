package com.example.team32gb.jobit.Lib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.team32gb.jobit.ListCandidateAcvitity;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Test.TestDuyet;
import com.example.team32gb.jobit.View.Admin.AdminShowDetailReportJobseekerActivity;
import com.example.team32gb.jobit.View.Admin.AdminShowDetailReportRecruiterActivity;
import com.example.team32gb.jobit.View.Admin.ShowDetailCompanyApprovalActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.annotation.RequiresApi;

import static android.app.NotificationManager.*;
import static com.example.team32gb.jobit.Utility.Config.DATE_SEND_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_ACCUSED_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_COMPANY_KEY;
import static com.example.team32gb.jobit.Utility.Config.ID_REPORT_KEY;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Map<String, String> dataMap = remoteMessage.getData();
            Bundle bundle = new Bundle();
            String type = dataMap.get("type");
            Log.e("kiemtraFCM", "type: "+type);
            if (type.equals("thongBaoUngVienApply")) {
                String idCompany = dataMap.get("idCompany");
                String idJob = dataMap.get("idJob");
                String nameJob = dataMap.get("nameJob");
                String timeJob = dataMap.get("timeJob");
                Log.e("kiemtraFCM", idCompany + " : " + idJob + " : " + nameJob + " : " + timeJob);
                bundle.putString("idCompany", dataMap.get("idCompany"));
                bundle.putString("idJob", dataMap.get("idJob"));
                bundle.putString("nameJob", dataMap.get("nameJob"));
                bundle.putString("timeJob", dataMap.get("timeJob"));
            }
            else if (type.equals("thongBaoAdminToCaoRecruiterMoi") || type.equals("thongBaoAdminToCaoJobSeekerMoi")){
                String idreport = dataMap.get(ID_REPORT_KEY);
                String datesend = dataMap.get(DATE_SEND_KEY);
                String idaccused = dataMap.get(ID_ACCUSED_KEY);
                Log.e("kiemtraFCM", "id report: "+ idreport + ", date: "+datesend +" id accused: "+ idaccused);
                bundle.putString(ID_REPORT_KEY, idreport);
                bundle.putString(DATE_SEND_KEY, datesend);
                bundle.putString(ID_ACCUSED_KEY, idaccused);
            }
            else if (type.equals("thongBaoAdminHoSoMoi")){
                bundle.putString(ID_COMPANY_KEY, dataMap.get(ID_COMPANY_KEY));
                bundle.putString(DATE_SEND_KEY, dataMap.get(DATE_SEND_KEY));
                Log.e("Thong bao admin", "Thong bao admin co ho so moi" + dataMap.get(ID_COMPANY_KEY)+"date: "+ dataMap.get(DATE_SEND_KEY));
            }


            Log.e("kiemtraFCM", type + " : " + title + " : " + body);
            showNotification(title, body, type, bundle);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(NotificationManager notificationManager) {
        NotificationChannel edmtChannel = null;
        edmtChannel = new NotificationChannel("com.example.team32gb.jobit.Lib.MyFirebaseMessagingService", "Jobit", IMPORTANCE_DEFAULT);
        edmtChannel.enableLights(true);
        edmtChannel.enableVibration(true);
        edmtChannel.setLightColor(Color.GREEN);
        edmtChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(edmtChannel);
    }

    public void showNotification(String title, String body, String type, Bundle bundle) {
        String channelId = "channelId";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent;
        if (type.equals("thongBaoUngVienApply")) {
            intent = new Intent(this, ListCandidateAcvitity.class);
            intent.putExtra("bundle", bundle);
        }
        else if(type.equals("thongBaoAdminToCaoRecruiterMoi")){
            intent = new Intent(this, AdminShowDetailReportRecruiterActivity.class);
            intent.putExtra("bundle", bundle);
        }
        else if (type.equals("thongBaoAdminToCaoJobSeekerMoi")){
            intent = new Intent(this, AdminShowDetailReportJobseekerActivity.class);
            intent.putExtra("bundle", bundle);
        }
        else if (type.equals("thongBaoAdminHoSoMoi")){
            intent = new Intent(this, ShowDetailCompanyApprovalActivity.class);
            intent.putExtra("bundle", bundle);
            Log.e("Thong bao admin", "Admin nhay activity show detail approval");
        }
        else {
            intent = new Intent(this, TestDuyet.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel edmtChannel = null;
            edmtChannel = new NotificationChannel(channelId, "Jobit", IMPORTANCE_DEFAULT);
            edmtChannel.enableLights(true);
            edmtChannel.enableVibration(true);
            edmtChannel.setLightColor(Color.GREEN);
            edmtChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(edmtChannel);

            builder = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.icon_app)
                    .setChannelId(channelId)
                    .setSound(alarmSound);
        } else {
            builder = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.icon_app)
                    .setSound(alarmSound);
        }


        notificationManager.notify(113, builder.build());

    }


}
