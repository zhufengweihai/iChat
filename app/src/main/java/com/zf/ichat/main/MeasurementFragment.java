package com.zf.ichat.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MeasurementFragment extends Fragment {
    private static final String ARG_POSITION = "POSITION";

    public static MeasurementFragment newInstance(int positon) {
        MeasurementFragment fragment = new MeasurementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, positon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int positon = getArguments().getInt(ARG_POSITION);
        return null;
    }


    public static class MeasurementPagerAdapter extends FragmentPagerAdapter {
        private String[] titles;

        public MeasurementPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return MeasurementFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}