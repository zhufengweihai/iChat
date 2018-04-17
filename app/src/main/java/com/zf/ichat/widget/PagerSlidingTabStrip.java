package com.zf.ichat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zf.ichat.R;

import static android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

public class PagerSlidingTabStrip extends RadioGroup {
    private static final int[] ATTRS = new int[]{android.R.attr.textSize, android.R.attr.textColor};

    private ViewPager pager;
    private int tabPadding = 24;
    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private Typeface tabTypeface = null;
    private int indicatorColor = 0xFF000000;
    private int tabBackgroundResId = 0;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, 12);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_tabPaddingLeftRight, 0);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_backgroundTab, tabBackgroundResId);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_textIndicatorColor, indicatorColor);
        a.recycle();
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        pager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectedTab(position);
            }
        });
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        for (int i = 0; i < pager.getAdapter().getCount(); i++) {
            addTab(i, pager.getAdapter().getPageTitle(i));
        }

        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            getViewTreeObserver().removeOnGlobalLayoutListener(this::notifyDataSetChanged);
            selectedTab(pager.getCurrentItem());
        });
    }

    private void addTab(int position, CharSequence title) {
        RadioButton tab = new RadioButton(getContext());
        tab.setButtonDrawable(null);
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tab.setBackgroundResource(tabBackgroundResId);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
        tab.setTypeface(tabTypeface, Typeface.NORMAL);
        tab.setTextColor(tabTextColor);
        tab.setOnClickListener(v -> pager.setCurrentItem(position));
        addView(tab, position, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
    }

    private void selectedTab(int currentPosition) {
        for (int i = 0; i < getChildCount(); i++) {
            RadioButton tab = (RadioButton) getChildAt(i);
            if (currentPosition == i) {
                tab.setTextColor(indicatorColor);
            } else {
                tab.setTextColor(tabTextColor);
            }
        }
    }
}
