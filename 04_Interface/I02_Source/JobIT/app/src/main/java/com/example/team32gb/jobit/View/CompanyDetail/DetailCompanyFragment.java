package com.example.team32gb.jobit.View.CompanyDetail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.team32gb.jobit.Model.SignUpAccountBusiness.InfoCompanyModel;
import com.example.team32gb.jobit.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailCompanyFragment extends Fragment implements CompanyDetailActivity.OnAboutDataReceivedListener {
    private TextView tvSize, tvType, tvIntroduce;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
//        Log.e("kiemtracontext",context.toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CompanyDetailActivity) context).setAboutDataListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_company_fragment,container,false);
        tvSize = v.findViewById(R.id.txtSizeCompanyDetail);
        tvType = v.findViewById(R.id.txtTypeCompanyDetail);
        tvIntroduce = v.findViewById(R.id.txtIntroduceCompany);
        return v;
    }

    @Override
    public void onDataReceived(InfoCompanyModel model) {
        tvSize.setText(model.getSize());
        tvType.setText(model.getType());
        tvIntroduce.setText(model.getIntroduce());
    }

    @Override
    public void onDataRatingReceived(String id) {

    }
}
