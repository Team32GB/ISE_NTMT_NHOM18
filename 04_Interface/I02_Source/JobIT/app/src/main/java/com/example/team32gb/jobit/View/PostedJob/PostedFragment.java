package com.example.team32gb.jobit.View.PostedJob;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Presenter.PostedJob.PresentInPostedJob;
import com.example.team32gb.jobit.Presenter.PostedJob.PresenterPostedJob;
import com.example.team32gb.jobit.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostedFragment extends Fragment implements ViewPostedJob {

    View v;
    private RecyclerView recyclerView;
    PresentInPostedJob presentInPostedJob;
    public PostedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_posted,container,false);
        recyclerView = v.findViewById(R.id.rvListPosted);
        return v;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presentInPostedJob = new PresenterPostedJob(this);
        presentInPostedJob.onCreate();
        presentInPostedJob.getPost(FirebaseAuth.getInstance().getUid());
    }

    @Override
    public void showPost(List<ItemPostJob> itemPostJobs) {

        Log.e("ktsize",itemPostJobs.size() + "");
        ViewAdapterPosted adapter = new ViewAdapterPosted(getContext(),itemPostJobs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
