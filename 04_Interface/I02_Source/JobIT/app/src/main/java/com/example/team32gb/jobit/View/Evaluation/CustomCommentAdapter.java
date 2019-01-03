package com.example.team32gb.jobit.View.Evaluation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.team32gb.jobit.R;

import java.util.ArrayList;

public class CustomCommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RatingModel> arr;

    public CustomCommentAdapter(Context context, ArrayList<RatingModel> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        if (arr == null)
            return 0;
        else
            return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View tag = inflater.inflate(R.layout.tag_comment, null);

        TextView title = tag.findViewById(R.id.txtCmtTitle);
        RatingBar ratingBar = tag.findViewById(R.id.rbComment);
        TextView date = tag.findViewById(R.id.txtCmtDate);
        TextView cmt = tag.findViewById(R.id.txtComment);

        title.setText(arr.get(position).getTitle());
        ratingBar.setRating(Float.parseFloat(Double.toString(arr.get(position).getRatingAverage())));
        date.setText(arr.get(position).getDateTime());
        cmt.setText(arr.get(position).getComment());

        return tag;
    }
}
