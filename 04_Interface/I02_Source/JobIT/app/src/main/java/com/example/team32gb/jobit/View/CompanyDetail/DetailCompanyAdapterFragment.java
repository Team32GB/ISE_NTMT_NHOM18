package com.example.team32gb.jobit.View.CompanyDetail;

import android.content.Context;
import android.os.Bundle;

import com.example.team32gb.jobit.View.Evaluation.FragmentRating;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DetailCompanyAdapterFragment extends FragmentPagerAdapter {
    final static int TAB1_DETAIL_COMPANY = 0;
    final static int TAB2_RATING = 1;
    final static int NUMBER_TAB =2;
    private Context context;
    private String idCompany;

    public DetailCompanyAdapterFragment(Context context,FragmentManager fm,String idCompany) {
        super(fm);
        this.context = context;
        this.idCompany = idCompany;
    }

    @Override
    public Fragment getItem(int position) {
//        DetailCompanyFragment fm = new DetailCompanyFragment();
//        return fm;

        switch (position) {
            case TAB1_DETAIL_COMPANY:
                DetailCompanyFragment fm = new DetailCompanyFragment();
                return fm;
            case TAB2_RATING:
                FragmentRating fm2= new FragmentRating();
                Bundle bundle = new Bundle();
                bundle.putString("idCompany",idCompany);
                fm2.setArguments(bundle);
                return fm2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_TAB;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB1_DETAIL_COMPANY:
                return "CHI TIẾT CÔNG TY";
            case TAB2_RATING:
                return "ĐÁNH GIÁ CÔNG TY";
            default:
                return null;
        }
    }
}
