package com.example.team32gb.jobit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.team32gb.jobit.Utility.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAdapterInvite extends RecyclerView.Adapter<ViewAdapterInvite.MyViewHolder> {
    Context context;
    List<DataApplied> mData;

    public ViewAdapterInvite(Context context,List<DataApplied> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewAdapterInvite.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_invite,viewGroup,false);
        return new ViewAdapterInvite.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewAdapterInvite.MyViewHolder myViewHolder, final int i) {
        myViewHolder.txtName.setText(mData.get(i).getName());
        Log.e("kiemtratime1",mData.get(i).getName());
        myViewHolder.txtTime.setText(Util.getSubTime(mData.get(i).getDayApplied()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtTime;
        private CircleImageView imgAvatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgAvatar= itemView.findViewById(R.id.imgAvatar);
        }
    }
}
