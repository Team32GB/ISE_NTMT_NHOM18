package com.example.team32gb.jobit.View.Evaluation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.team32gb.jobit.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentRating extends Fragment implements View.OnClickListener {
    private Context context;
    private ListView lstRatingMem;
    private ListView lstComment;
    private Button btnAddRating;
    private Dialog dialog;
    private static float ratingAverage = 0.f;
    private static float rating1  = 0.f;
    private static float rating2  = 0.f;
    private static float rating3  = 0.f;
    private static float rating4  = 0.f;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rating, container, false);

        lstRatingMem = v.findViewById(R.id.lstRatingMem);
        btnAddRating = v.findViewById(R.id.btnAddReview);
        btnAddRating.setOnClickListener(this);
//        ArrayList<RatingMem> arrRatingMem=new ArrayList<RatingMem>();
//
//        RatingMem rt1= new RatingMem("Salary",1);
//        RatingMem rt2=new RatingMem("Environment",2);
//        RatingMem rt3=new RatingMem("Pressure",3);
//
//        arrRatingMem.add(rt1);
//        arrRatingMem.add(rt2);
//        arrRatingMem.add(rt3);
//
//        CustomRatingMemAdapter customRatingMemAdapter = new CustomRatingMemAdapter(context,arrRatingMem);
////        lstRatingMem.setAdapter(customRatingMemAdapter);

        lstComment = v.findViewById(R.id.lstComment);
        ArrayList<Comment> arrComment = new ArrayList<>();

        Comment cmt1 = new Comment("Lương ổn định, sếp tốt", 4, "10/10/2018", "Lương cao so với mặt bằng chung. Sếp thân thiện. Chịu lắng nghe ý kiến của nhân viên.");
        Comment cmt2 = new Comment("Hesitation", 3.5f, "10/10/2018", "Bad");
        Comment cmt3 = new Comment("Conversation", 4.5f, "10/10/2018", "Worse");

        arrComment.add(cmt1);
        arrComment.add(cmt2);
        arrComment.add(cmt3);

        CustomCommentAdapter customCommentAdapter = new CustomCommentAdapter(context, arrComment);
        lstComment.setAdapter(customCommentAdapter);
        setListViewHeightBasedOnChildren(lstComment);
        return v;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAddReview:
                setUpDialogReview();
                break;
            default:
                break;
        }
    }

    public void setUpDialogReview() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_diaglog_rating,null );
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.70);

        final RatingBar rbAverage,rb1,rb2,rb3,rb4;
        final TextView tvAverage,tvrb1,tvrb2,tvrb3,tvrb4;
        rbAverage = dialogView.findViewById(R.id.AverageRatingBar);
        rb1 = dialogView.findViewById(R.id.rb1);
        rb2 = dialogView.findViewById(R.id.rb2);
        rb3 = dialogView.findViewById(R.id.rb3);
        rb4 = dialogView.findViewById(R.id.rb4);
        tvAverage = dialogView.findViewById(R.id.txtAverageRate);
        tvrb1 = dialogView.findViewById(R.id.tvStar1);
        tvrb2 = dialogView.findViewById(R.id.tvStar2);
        tvrb3 = dialogView.findViewById(R.id.tvStar3);
        tvrb4 = dialogView.findViewById(R.id.tvStar4);

        rbAverage.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
                    tvAverage.setText(Float.toString(rating));
                    rb1.setRating(rating);
                    rb2.setRating(rating);
                    rb3.setRating(rating);
                    rb4.setRating(rating);
                    rating1 = rating2 = rating3 = rating4 =  rating;
                    tvrb1.setText(Float.toString(rating));
                    tvrb2.setText(Float.toString(rating));
                    tvrb3.setText(Float.toString(rating));
                    tvrb4.setText(Float.toString(rating));
                }

            }
        });

        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating1 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4)/4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb1.setText(Float.toString(rating));
            }
        });
        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating2 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4)/4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb2.setText(Float.toString(rating));
            }
        });

        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating3 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4)/4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb3.setText(Float.toString(rating));
            }
        });
        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating4 = rating;
                ratingAverage = (rating1 + rating2 + rating3 + rating4)/4;
                ratingAverage = Math.round(ratingAverage * 10) / 10.0f;
                rbAverage.setRating(ratingAverage);
                tvAverage.setText(Float.toString(ratingAverage));
                tvrb4.setText(Float.toString(rating));
            }
        });

        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(width, height);
        dialog.setTitle("Đánh giá nhà tuyển dụng");
        dialog.show();
    }


}
