package com.zf.ichat.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class EffectTextView extends AppCompatTextView {
    public EffectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPressed(boolean pressed) {
        updateView(pressed);
        super.setPressed(pressed);
    }

    /**
     * 根据是否按下去设置滤镜
     */
    private void updateView(boolean pressed) {
        if (pressed) {
            setFilter();
        } else {
            removeFilter();
        }
    }

    private void setFilter() {
        //先获取设置的src图片
        Drawable drawable = getBackground();
        //当src图片为Null，获取背景图片
        if (drawable != null) {
            //设置滤镜
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable = getBackground();
        if (drawable != null) {
            //清除滤镜
            drawable.clearColorFilter();
        }
    }
}