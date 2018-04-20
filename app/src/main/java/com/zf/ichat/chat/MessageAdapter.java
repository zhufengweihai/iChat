package com.zf.ichat.chat;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;

public class MessageAdapter extends PagedListAdapter<Message, TextViewHolder> {
    private Conversation conversation;

    public MessageAdapter(Conversation conversation) {
        super(getDiffCallback());
        this.conversation = conversation;
    }

    @NonNull
    private static DiffUtil.ItemCallback<Message> getDiffCallback() {
        return new DiffUtil.ItemCallback<Message>() {
            @Override
            public boolean areItemsTheSame(Message oldItem, Message newItem) {
                return oldItem.getContactId() == newItem.getContactId();
            }

            @Override
            public boolean areContentsTheSame(Message oldItem, Message newItem) {
                return oldItem == newItem;
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextViewHolder(parent,conversation);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }
}