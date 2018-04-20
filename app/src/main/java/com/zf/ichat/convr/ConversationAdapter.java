package com.zf.ichat.convr;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.zf.ichat.data.Conversation;

public class ConversationAdapter extends PagedListAdapter<Conversation, ConversationViewHolder> {
    public ConversationAdapter() {
        super(getDiffCallback());
    }

    @NonNull
    private static DiffUtil.ItemCallback<Conversation> getDiffCallback() {
        return new DiffUtil.ItemCallback<Conversation>() {
            @Override
            public boolean areItemsTheSame(Conversation oldItem, Conversation newItem) {
                return oldItem.getContactId() == newItem.getContactId();
            }

            @Override
            public boolean areContentsTheSame(Conversation oldItem, Conversation newItem) {
                return oldItem == newItem;
            }
        };
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }
}