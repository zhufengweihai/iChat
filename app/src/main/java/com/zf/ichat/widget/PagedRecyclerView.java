package com.zf.ichat.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PagedRecyclerView extends RecyclerView {
    private boolean scrollToNext = false;
    private int dPos = 0;
    private int distance;

    public PagedRecyclerView(Context context) {
        this(context, null);
    }

    public PagedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        distance = getResources().getDisplayMetrics().widthPixels / 6;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            updatePage(true);
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            updatePage(false);
        }
        return super.onTouchEvent(e);
    }


    private void updatePage(boolean pressed) {
        if (pressed) {
            scrollToNext = true;
        } else if (scrollToNext) {
            int position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() + dPos;
            if (position >= 0 && position < getAdapter().getItemCount()) {
                smoothScrollToPosition(position);
            }
            scrollToNext = false;
            dPos = 0;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (scrollToNext) {
            if (dx >= distance) {
                dPos = 1;
            } else if (dx <= -distance) {
                dPos = -1;
            }
        }
    }
}