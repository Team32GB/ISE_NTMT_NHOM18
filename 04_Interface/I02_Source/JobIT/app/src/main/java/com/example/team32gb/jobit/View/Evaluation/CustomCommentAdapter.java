package com.example.team32gb.jobit.View.Evaluation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.Utility.Util;
import com.example.team32gb.jobit.View.SignIn.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.appcompat.widget.PopupMenu;

public class CustomCommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RatingModel> arr;
    private FragmentCommunication communication;

    public CustomCommentAdapter(Context context, ArrayList<RatingModel> arr,FragmentCommunication communication) {
        this.context = context;
        this.arr = arr;
        this.communication = communication;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View tag = inflater.inflate(R.layout.tag_comment, null);

        TextView title = tag.findViewById(R.id.txtCmtTitle);
        RatingBar ratingBar = tag.findViewById(R.id.rbComment);
        TextView date = tag.findViewById(R.id.txtCmtDate);
        TextView cmt = tag.findViewById(R.id.txtComment);
        final ImageButton btnMore = tag.findViewById(R.id.btnMore);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getUid() != null) {
                    if(v.getId() == R.id.btnMore){
                        showMenu(btnMore,position);
                    }
                } else {
                    Util.jumpActivity(context,SignInActivity.class);
                }
            }
        });

        title.setText(arr.get(position).getTitle());
        ratingBar.setRating(Float.parseFloat(Double.toString(arr.get(position).getRatingAverage())));
        date.setText(arr.get(position).getDateTime());
        cmt.setText(arr.get(position).getComment());

        return tag;
    }

    public void showMenu(View v, final int position)
    {
        PopupMenu popup = new PopupMenu(context,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.btnReport) {
                    communication.respond(position);
                }
                return true;
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.button_menu, popup.getMenu());
        popup.show();
    }
}
