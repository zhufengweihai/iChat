package com.zf.ichat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.util.TypedValue;
import android.view.View;

public class SuspensionDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    private Rect bound;

    private int titleHeight;//title的高
    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF999999");

    public SuspensionDecoration(Context context) {
        super();
        paint = new Paint();
        bound = new Rect();
        Resources resources = context.getResources();
        titleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, resources.getDisplayMetrics());
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, resources.getDisplayMetrics());
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            long index = parent.getAdapter().getItemId(layoutParams.getViewLayoutPosition());
            if (index >= 0) {
                drawTitleArea(c, left, right, child, layoutParams, (int) index);
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法,最先调用，绘制在最下层
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, LayoutParams params, int index) {
        paint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - titleHeight, right, child.getTop() - params.topMargin,
                paint);
        paint.setColor(COLOR_TITLE_FONT);

        String indexString = IndexBar.INDEX_STRING[index];
        paint.getTextBounds(indexString, 0, indexString.length(), bound);
        c.drawText(indexString, child.getPaddingLeft(), child.getTop() - params.topMargin - (titleHeight / 2 - bound
                .height() / 2), paint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (parent.getAdapter().getItemId(position) >= 0) {
            outRect.set(0, titleHeight, 0, 0);
        }
    }

    public interface SuspensionTitle {
        String getTitle();
    }
}
