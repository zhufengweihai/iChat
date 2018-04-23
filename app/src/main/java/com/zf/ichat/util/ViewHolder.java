package com.zf.ichat.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zf.ichat.data.Message;

/**
 * @author zhufeng
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 绑定数据到视图控件
     * @param data 数据
     */
    public abstract void bindTo(T data);
}
