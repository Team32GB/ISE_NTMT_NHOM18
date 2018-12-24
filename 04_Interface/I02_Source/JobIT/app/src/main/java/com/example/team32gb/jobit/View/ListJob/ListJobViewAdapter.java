package com.example.team32gb.jobit.View.ListJob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Utility.Config;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.JobDetail.DetailJobActivity;
import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.View.ListJobSearch.ItemClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListJobViewAdapter extends RecyclerView.Adapter<ListJobViewAdapter.MyViewHolder> {

    Context context;
    List<ItemPostJob> mdata;

    public ListJobViewAdapter(Context context, List<ItemPostJob> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        Log.e("kiemtraid", "oncreateViewHolder" + i);
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.activity_item_listjob, viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
//        Log.e("kiemtra111",mdata.get(i).getDataPostJob().getNameJob() + ": " + i);
        final String idCompany = mdata.get(i).getDataPostJob().getIdCompany();
        final String idJob = mdata.get(i).getDataPostJob().getIdJob();

        myViewHolder.txtNameJob.setText(mdata.get(i).getDataPostJob().getNameJob());
        myViewHolder.txtNameCompany.setText(mdata.get(i).getDataPostJob().getNameCompany());
        myViewHolder.txtProvince.setText(mdata.get(i).getDataPostJob().getProvince());
        myViewHolder.txtTime.setText(Util.getSubTime(mdata.get(i).getDataPostJob().getTime()));

        final String uid = FirebaseAuth.getInstance().getUid();
        if(uid != null && !uid.isEmpty()) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot dsSaved = dataSnapshot.child("daLuus").child(uid).child(idCompany);
                    DataSnapshot dsApplied = dataSnapshot.child("Applieds").child(uid).child(idCompany);
                    Log.e("kiemtraaaaaa",dataSnapshot.toString());
                    if(dsSaved.hasChild(idJob)) {
                        mdata.get(i).setChecked(true);
                        myViewHolder.cbSaved.setChecked(true);
                    }

                    if(dsApplied.hasChild(idJob)) {
                        mdata.get(i).setApplied(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            myViewHolder.cbSaved.setEnabled(false);
        }

        if(idCompany != null) {
            long ONE_MEGABYTE = 1024 * 1024;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Config.REF_FOLDER_LOGO).child(idCompany);
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    myViewHolder.imageView.setImageBitmap(bitmap);
                    Util.saveImageToFolderLogoLocal(bitmap,idCompany);
                }
            });
        }
        String minSalary = mdata.get(i).getDataPostJob().getMinSalary();
        final String maxSalary = mdata.get(i).getDataPostJob().getMaxSalary();
        myViewHolder.txtSalary.setText("Từ $" + minSalary + " đến $" + maxSalary);
        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(context.getApplicationContext(), DetailJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                Log.e("kiemtraanh",mdata.get(i).getDataPostJob().getIdCompany());
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
        private TextView txtProvince;
        private CheckBox cbSaved;
        private MaterialCardView item_listjob;
        private ImageView imageView;
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
            txtProvince = itemView.findViewById(R.id.txtProvince);
            imageView = itemView.findViewById(R.id.imgAvatarCompany);
            item_listjob = itemView.findViewById(R.id.item_listjob);
            cbSaved = itemView.findViewById(R.id.imgFav);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
