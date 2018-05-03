package com.zf.ichat.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.support.v4.view.ViewPager;

public class PagerTabGroup extends LinearLayout implements OnClickListener {
    public PagerTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof PagerTab || view instanceof DMTabTextView) {
                view.setOnClickListener(this);
            }
        }
    }

    public void setChecked(int position) {
        setChecked(position, false);
    }

    private void setChecked(int position, boolean byUser) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof PagerTab || view instanceof DMTabTextView) {
                view.setSelected(position == i);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setChecked(position);
            }
        });
    }

    /**
     * 设置是否有更新
     *
     * @param position
     * @param hasNew
     */
    public void setHasNew(int position, boolean hasNew) {
        PagerTab button = (PagerTab) getChildAt(position);
        if (button != null) {
            button.setHasNew(hasNew);
        }
    }

    /**
     * 设置未读数
     *
     * @param position
     * @param count
     */
    public void setUnreadCount(int position, int count) {
        PagerTab button = (PagerTab) getChildAt(position);
        if (button != null) {
            button.setUnreadCount(count);
        }
    }

    @Override
    public void onClick(View v) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (v == view) {
                setChecked(i, true);
                break;
            }
        }
    }
}
