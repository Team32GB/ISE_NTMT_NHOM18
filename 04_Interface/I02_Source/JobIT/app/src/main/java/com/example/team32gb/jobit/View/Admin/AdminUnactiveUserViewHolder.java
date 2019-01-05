package com.example.team32gb.jobit.View.Admin;

import android.view.View;
import android.widget.TextView;

import com.example.team32gb.jobit.R;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminUnactiveUserViewHolder extends RecyclerView.ViewHolder {
    public interface ClickListner{
        public void onItemClick(View view, int position);
    }
    TextView txtUnactiveUserName;
    TextView txtDateUnactiveUser;
    AdminUnactiveUserViewHolder.ClickListner mClickListner;

    public AdminUnactiveUserViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUnactiveUserName = itemView.findViewById(R.id.txtUnactiveUserName);
        txtDateUnactiveUser = itemView.findViewById(R.id.txtDateUnactiveUser);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListner.onItemClick(v, getAdapterPosition());
            }
        });
    }

    public void SetOnClickListener(AdminUnactiveUserViewHolder.ClickListner clickListner){
        this.mClickListner = clickListner;
    }
}
