package com.zf.ichat.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.zf.ichat.BaseActivity;
import com.zf.ichat.R;
import com.zf.ichat.main.MeasurementFragment.MeasurementPagerAdapter;
import com.zf.ichat.widget.PagerSlidingTabStrip;

public class MeasurementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.container);
        String[] titles = getResources().getStringArray(R.array.title_main);
        MeasurementPagerAdapter pagerAdapter = new MeasurementPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);
        ((PagerSlidingTabStrip)findViewById(R.id.pagertab)).setViewPager(viewPager);
    }
}
