package com.example.team32gb.jobit.View.WaitingInterviewNTD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team32gb.jobit.DataApplied;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.CreateCV.CreateCVActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAdapterInterview extends RecyclerView.Adapter<ViewAdapterInterview.MyViewHolder>{
    Context context;
    List<DataApplied> mData;

    public ViewAdapterInterview(Context context,List<DataApplied> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewAdapterInterview.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_applied,viewGroup,false);
        return new ViewAdapterInterview.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewAdapterInterview.MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtName.setText(mData.get(i).getName());
        Log.e("kiemtratime1",mData.get(i).getName());
        myViewHolder.txtTime.setText(Util.getSubTime(mData.get(i).getDayApplied()));

        myViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kiemtraLog",mData.get(i).getIdJobSeeker());
                DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
                String idCompany = mData.get(i).getIdCompany();
                String idJob = mData.get(i).getIdJob();
                String idJobSeeker = mData.get(i).getIdJobSeeker();
                String time = mData.get(i).getDayApplied();

                DatabaseReference drChoPV = nodeRoot.child("choPhongVanNTDs").child(idCompany).child(idJob).child(idJobSeeker);
                drChoPV.removeValue();
                DatabaseReference drChoPVNTV = nodeRoot.child("choPhongVanNTVs").child(idJobSeeker).child(idCompany).child(idJob);
                drChoPVNTV.removeValue();

                DatabaseReference drMoiLam = nodeRoot.child("moiLamNTDs").child(idCompany).child(idJob).child(idJobSeeker).child("timeApplied");
                drMoiLam.setValue(time);
                DatabaseReference drMoiLamNTV = nodeRoot.child("moiLamNTVs").child(idJobSeeker).child(idCompany).child(idJob).child("timeApplied");
                drMoiLamNTV.setValue(time);

                mData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getItemCount());

                Toast.makeText(context, "Duyệt thành công", Toast.LENGTH_SHORT).show();
            }
        });
        myViewHolder.btnWacthCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),CreateCVActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idJobSeeker",mData.get(i).getIdJobSeeker());
                intent.putExtra("bundle",bundle);
                context.startActivity(intent);
            }
        });
        myViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kiemtraLog",mData.get(i).getIdJobSeeker());
                DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
                String idCompany = mData.get(i).getIdCompany();
                String idJob = mData.get(i).getIdJob();
                String idJobSeeker = mData.get(i).getIdJobSeeker();
                String time = mData.get(i).getDayApplied();

                DatabaseReference drChoPV = nodeRoot.child("choPhongVanNTDs").child(idCompany).child(idJob).child(idJobSeeker);
                drChoPV.removeValue();

                DatabaseReference drMoiLamNTV = nodeRoot.child("loaiMoiLams").child(idJobSeeker).child(idCompany).child(idJob).child("timeApplied");
                drMoiLamNTV.setValue(time);

                mData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getItemCount());

                Toast.makeText(context, "Loại thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtTime;
        private Button btnAccept, btnDelete, btnWacthCV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnWacthCV = itemView.findViewById(R.id.btnWatchCV);
        }
    }
}
