package com.example.team32gb.jobit.Utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.Model.PostJob.DataPostJob;
import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.View.JobDetail.DetailJobActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

public class Util {
    private static boolean isSuccess = false;
    public static ItemPostJob parserJSONToItemPost(JSONObject jsonObject) {
        ItemPostJob itemPostJob = new ItemPostJob();
        DataPostJob dataPostJob = new DataPostJob();
        try {
            dataPostJob.setTime(jsonObject.getString("time"));
            dataPostJob.setDescription(jsonObject.getString("description"));
            dataPostJob.setEach(jsonObject.getString("each"));
            dataPostJob.setMaxSalary(jsonObject.getString("maxSalary"));
            dataPostJob.setMinSalary(jsonObject.getString("minSalary"));
            dataPostJob.setNameJob(jsonObject.getString("nameJob"));
            dataPostJob.setNumberEmployer(jsonObject.getString("numberEmployer"));
            dataPostJob.setQualification(jsonObject.getString("qualification"));
            dataPostJob.setTypeJob(jsonObject.getString("typeJob"));
            dataPostJob.setIdCompany(jsonObject.getString("idCompany"));
            Log.e("kiemtraAlgolia",dataPostJob.getIdCompany());
            dataPostJob.setIdJob(jsonObject.getString("idJob"));
            dataPostJob.setNameCompany(jsonObject.getString("nameCompany"));
          //  dataPostJob.setAvatar(jsonObject.getString("avatar"));
            dataPostJob.setProvince(jsonObject.getString("province"));

            itemPostJob.setDataPostJob(dataPostJob);
            return itemPostJob;
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return null;
    }

    public static void jumpActivity(Context context, Class mclass) {
//        Log.e("kiemtrajump",context.toString() + "");
        Intent intent = new Intent(context, mclass);
        context.startActivity(intent);
    }

    public static void jumpActivityRemoveStack(Context context, Class mclass) {
//        Log.e("kiemtrajump",context.toString() + "");
        Intent intent = new Intent(context, mclass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void resetDataLocal(SharedPreferences sharedPreferences) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putInt(Config.USER_TYPE,0);
//        editor.putString(Config.EMAIL_USER,"");
//        editor.putString(Config.PASSWORD_USER,"");
//        editor.putString(Config.NAME_USER,"");
//        editor.putBoolean(Config.IS_LOGGED,false);
//        editor.apply();
    }
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String getSubTime(String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            Date date = sdf.parse(timeFormat);
            long time = date.getTime();
            long currentTime = (new Date()).getTime();
            long difference = currentTime - time;
            int subDay = (int) (difference / Config.MILISECONDOFDAY);
            Log.e("kiemtratime",currentTime + " : " + time + " : " + difference);


            if (subDay > 0) {
                return subDay + " ngày trước";
            } else {
                int subHour = (int) difference / (1000 * 60 * 60);
                if (subHour > 0) {
                    return subHour + " giờ trước";
                } else {
                    int subMinute = (int) difference / (1000 * 60);
                    return subMinute + " phút trước";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setSubTime(String timeFormat, TextView txtTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            Date date = sdf.parse(timeFormat);
            long time = date.getTime();
            long currentTime = (new Date()).getTime();
            long difference = currentTime - time;
            int subDay = (int) (difference / Config.MILISECONDOFDAY);
            if (subDay > 0) {
                txtTime.setText(subDay + " ngày trước");
            } else {
                int subHour = (int) difference / (1000 * 60 * 60);
                if (subHour > 0) {
                    txtTime.setText(subHour + " giờ trước");
                } else {
                    int subMinute = (int) difference / (1000 * 60);
                    txtTime.setText(subMinute + " phút trước");
                }
            }

        } catch (ParseException e) {
            txtTime.setText(timeFormat);
            e.printStackTrace();
        }
    }

    public static void signOut(FirebaseAuth firebaseAuth, Context context) {
        firebaseAuth.signOut();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Config.IS_LOGGED, false);
        editor.putBoolean(Config.REGESTERED_INFO, false);
        editor.apply();
    }

    public static int getPositionSpinnerFromString(String string, List<String> ls) {
        for (int i = 0; i < ls.size(); i++) {
            if (ls.get(i).equals(string)) {
                return i;
            }
        }
        return 0;
    }

    public static void saveImageToLocal(Bitmap bitmap, String uid) {
        String avatarPath = Environment.getExternalStorageDirectory() + "/avatar" + "/" + uid + ".jpg";
//        Log.e("kiemtraImage", avatarPath);
        File file = new File(avatarPath);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Log.e("kiemtraImage", avatarPath);

    }

    public static void saveImageToFolderLogoLocal(Bitmap bitmap, String uid) {
        String avatarPath = Environment.getExternalStorageDirectory() + "/avatar" + "/" + uid + ".jpg";
//        Log.e("kiemtraImage", avatarPath);
        File file = new File(avatarPath);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Log.e("kiemtraImage", avatarPath);

    }

    public static void loadImageFromLocal(AppCompatImageButton btnAvatar, String idCompany) {
        String avatarPath = Environment.getExternalStorageDirectory() + "/avatar" + "/" + idCompany + ".jpg";
//        Log.e("kiemtraanh", avatarPath);
        Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
        if (bitmap != null && avatarPath != null && !avatarPath.isEmpty()) {
            btnAvatar.setBackground(new BitmapDrawable(bitmap));
        }
    }

    public static void loadImageFromFolderLogoLocal(AppCompatImageButton btnAvatar, String idCompany) {
        String avatarPath = Environment.getExternalStorageDirectory() + "/logo" + "/" + idCompany + ".jpg";
//        Log.e("kiemtraanh", avatarPath);
        Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
        if (bitmap != null && avatarPath != null && !avatarPath.isEmpty()) {
            btnAvatar.setBackground(new BitmapDrawable(bitmap));
        }
    }

    public static String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(new Date());
    }

    public static boolean saveJob(final Context context, DatabaseReference nodeRoot, String uid, String idCompany, String idJob) {
        String currentDate = Util.getCurrentDay();
        DatabaseReference dfDaLuus = nodeRoot.child("daLuus").child(uid).child(idCompany).child(idJob).child("timeSaved");
        dfDaLuus.setValue(currentDate).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(context, "Lưu tin thất bại", Toast.LENGTH_LONG).show();
                isSuccess = false;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(context, "Lưu tin thành công", Toast.LENGTH_LONG).show();
                isSuccess = true;
            }
        });
        return isSuccess;
    }

    public static void RemoveSaveJob(final Context context, DatabaseReference nodeRoot, String uid, String idCompany, String idJob) {
        DatabaseReference dfRemove = nodeRoot.child("daLuus").child(uid).child(idCompany).child(idJob);
        dfRemove.removeValue();
        Toast.makeText(context,"Xóa thành công",Toast.LENGTH_LONG).show();
    }
}
