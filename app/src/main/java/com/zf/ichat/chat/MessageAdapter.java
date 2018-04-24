package com.zf.ichat.chat;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;
import com.zf.ichat.data.MessageType;

public class MessageAdapter extends PagedListAdapter<Message, MessageViewHolder> {
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
        return getItem(position).getType().ordinal();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (MessageType.values()[viewType]) {
            case Image:
                return new ImageViewHolder(parent, conversation);
            default:
                return new TextViewHolder(parent, conversation);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        boolean showTime = false;
        Message message = getItem(position);
        if (position > 0) {
            showTime = message.getCreateTime() - getItem(position + 1).getCreateTime() >= 60 * 1000;
        }
        holder.bindTo(message, showTime);
    }

    @Override
    public void onViewRecycled(@NonNull MessageViewHolder holder) {
        holder.onViewRecycled();
    }
}