package com.example.team32gb.jobit.View.Admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.View.Feedback.FeedbackModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminFeedbackAdaptor extends RecyclerView.Adapter<AdminFeedbackAdaptor.AdminFeedbackViewHolder> {
    ArrayList<FeedbackModel> dataArray = new ArrayList<>() ;

    public AdminFeedbackAdaptor(ArrayList<FeedbackModel> dataArray) {
        this.dataArray = dataArray;
    }

    @NonNull
    @Override
    public AdminFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.admin_feedback_item_row, parent, false);
        AdminFeedbackViewHolder viewHolder = new AdminFeedbackViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFeedbackViewHolder holder, int position) {
        FeedbackModel model =  dataArray.get(position);
        holder.txtContentFeedback.setText(model.getContent());
        holder.txtDateSendFeedback.setText(model.getTime());
        Log.e("viewholder feedback", "da set text: " + model.getContent());
    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    public class AdminFeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView txtContentFeedback;
        TextView txtDateSendFeedback;
        public AdminFeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContentFeedback  = itemView.findViewById(R.id.txtContentFeedback);
            txtDateSendFeedback = itemView.findViewById(R.id.txtDateSendFeedback);
        }
    }
}
