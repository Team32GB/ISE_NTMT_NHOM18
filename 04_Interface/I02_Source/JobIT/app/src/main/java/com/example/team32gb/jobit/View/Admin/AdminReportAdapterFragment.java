package com.example.team32gb.jobit.View.Admin;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdminReportAdapterFragment extends FragmentPagerAdapter {
    final static int TAB1_EMPLOYEE = 0;
    final static int TAB2_EMPLYER = 1;
    final static int NUMBER_TAB =2;
    Context context;

    public AdminReportAdapterFragment(AdminReportActivity adminReportActivity, FragmentManager fm) {
        super(fm);
        context= adminReportActivity.getApplicationContext();
    }

    @Override
    public androidx.fragment.app.Fragment getItem(int i) {
        switch (i) {
            case TAB1_EMPLOYEE:
                AdminReportFragmentTab1 fm = new AdminReportFragmentTab1();
//                fm.setContext(context);
                return fm;
            case TAB2_EMPLYER:
                AdminReportFragmentTab2 fm2= new AdminReportFragmentTab2();
//                fm2.setContext(context);
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
            case TAB1_EMPLOYEE:
                return "Người tìm việc";
            case TAB2_EMPLYER:
                return "Nhà tuyển dụng";
            default:
                return null;
        }
    }
}
