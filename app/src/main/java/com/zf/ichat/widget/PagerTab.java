package com.zf.ichat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zf.ichat.R;

public class PagerTab extends RelativeLayout {
    private View tab_tip;
    private TextView unread_count;
    private boolean isChecked = false;

    public PagerTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs);
    }

    public PagerTab(Context context) {
        super(context);
    }

    public PagerTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.main_tab_bottom_item, this);
        ImageView image = contentView.findViewById(R.id.iv_tab_item_icon);
        TextView tab_button = contentView.findViewById(R.id.tv_tab_item_icon);
        tab_tip = contentView.findViewById(R.id.tab_tip);
        unread_count = contentView.findViewById(R.id.unread_count);
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.tab_button);
        Drawable d = a.getDrawable(R.styleable.tab_button_drawableTop);
        String text = a.getString(R.styleable.tab_button_tabtext);
        String attrStr = a.getString(R.styleable.tab_button_drawableTopAttr);
        a.recycle();
        tab_button.setText(text);
        if (d != null) {
            image.setImageDrawable(d);
        } else {
            TypedValue typedValue = new TypedValue();
            int attr = getResources().getIdentifier(attrStr, "attr", context.getPackageName());
            getContext().getTheme().resolveAttribute(attr, typedValue, true);
            image.setImageResource(typedValue.resourceId);
        }
    }

    public void setHasNew(boolean hasNew) {
        if (tab_tip != null) {
            tab_tip.setVisibility(hasNew ? View.VISIBLE : View.GONE);
        }
    }

    public void setUnreadCount(int count) {
        unread_count.setText(String.valueOf(count));
        unread_count.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }
}
