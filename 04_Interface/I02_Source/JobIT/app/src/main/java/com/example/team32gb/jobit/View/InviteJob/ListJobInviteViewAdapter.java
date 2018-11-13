package com.example.team32gb.jobit.View.InviteJob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.JobDetail.DetailJobActivity;
import com.example.team32gb.jobit.View.ListJobSearch.ItemClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListJobInviteViewAdapter extends RecyclerView.Adapter<ListJobInviteViewAdapter.MyViewHolder>{
    Context context;
    List<ItemPostJob> mdata;

    public ListJobInviteViewAdapter(Context context, List<ItemPostJob> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ListJobInviteViewAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        Log.e("kiemtraid", "oncreateViewHolder" + i);
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.activity_item_listjob, viewGroup, false);
        final ListJobInviteViewAdapter.MyViewHolder viewHolder = new ListJobInviteViewAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListJobInviteViewAdapter.MyViewHolder myViewHolder, final int i) {
        Log.e("kiemtraid", "onBindViewHolder" + mdata.get(i).getTimeApplied());
        myViewHolder.txtNameJob.setText(mdata.get(i).getDataPostJob().getNameJob());
        myViewHolder.txtNameCompany.setText(mdata.get(i).getNameCompany());
        myViewHolder.txtTime.setText(Util.getSubTime(mdata.get(i).getTimeApplied()));
        myViewHolder.txtTime.setText(Util.getSubTime(mdata.get(i).getDataPostJob().getTime()));
        String minSalary = mdata.get(i).getDataPostJob().getMinSalary();
        String maxSalary = mdata.get(i).getDataPostJob().getMaxSalary();
        myViewHolder.txtSalary.setText("Từ $" + minSalary + " đến $" + maxSalary);

        long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Config.REF_FOLDER_AVATAR).child(mdata.get(i).getIdCompany());
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                myViewHolder.imageView.setImageBitmap(bitmap);
            }
        });

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(context.getApplicationContext(), DetailJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("bundle", mdata.get(i));
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNameJob;
        private TextView txtNameCompany;
        private TextView txtTime;
        private TextView txtSalary;
        private ImageView imageView;
        // private RelativeLayout item_listjob;
        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtNameJob = itemView.findViewById(R.id.txtTenCV);
            txtNameCompany = itemView.findViewById(R.id.txtTenCT);
            txtTime = itemView.findViewById(R.id.txtThơiGian);
            txtSalary = itemView.findViewById(R.id.txtSalary);


            imageView = itemView.findViewById(R.id.imgAvatarCompany);
            //    item_listjob = itemView.findViewById(R.id.item_listjob);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
