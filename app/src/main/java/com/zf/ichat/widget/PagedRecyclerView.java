package com.zf.ichat.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

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
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            if (scrollToNext) {
                int position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() + dPos;
                if (position >= 0 && position < getAdapter().getItemCount()) {
                    smoothScrollToPosition(position);
                }
                scrollToNext = false;
                dPos = 0;
            }
        } else {
            scrollToNext = true;
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