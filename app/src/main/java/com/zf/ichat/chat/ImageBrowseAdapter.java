package com.zf.ichat.chat;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.zf.ichat.data.Message;

public class ImageBrowseAdapter extends PagedListAdapter<Message, ImageBrowseViewHolder> {
    private int currentPos;

    public ImageBrowseAdapter(int currentPos) {
        super(getDiffCallback());
        this.currentPos = currentPos;
    }

    @NonNull
    private static DiffUtil.ItemCallback<Message> getDiffCallback() {
        return new DiffUtil.ItemCallback<Message>() {
            @Override
            public boolean areItemsTheSame(Message oldItem, Message newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(Message oldItem, Message newItem) {
                return oldItem == newItem;
            }
        };
    }

    @NonNull
    @Override
    public ImageBrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageBrowseViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageBrowseViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull ImageBrowseViewHolder holder) {
        holder.onViewRecycled();
    }
}