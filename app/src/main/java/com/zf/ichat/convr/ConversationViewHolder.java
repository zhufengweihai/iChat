package com.zf.ichat.convr;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zf.ichat.R;
import com.zf.ichat.chat.ChatActivity;
import com.zf.ichat.data.Conversation;

public class ConversationViewHolder extends RecyclerView.ViewHolder {
    private final ImageView avatarView;
    private final TextView unreadView;
    private final TextView nameView;
    private final TextView contentView;
    private final TextView timeView;

    public ConversationViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_convr_item, parent, false));
        avatarView = itemView.findViewById(R.id.avatarView);
        unreadView = itemView.findViewById(R.id.unreadView);
        nameView = itemView.findViewById(R.id.nameView);
        contentView = itemView.findViewById(R.id.contentView);
        timeView = itemView.findViewById(R.id.timeView);
    }

    public void bindTo(Conversation convr) {
        Glide.with(avatarView).load(convr.getAvatarUrl()).into(avatarView);
        contentView.setText(convr.getMessage());
        nameView.setText(convr.getNickname());
        timeView.setText(String.valueOf(convr.getCreateTime()));
        unreadView.setText(String.valueOf(convr.getUnread()));
        itemView.setOnClickListener(v -> {
            Intent startIntent = new Intent(itemView.getContext(), ChatActivity.class);
            startIntent.putExtra("conversation", convr);
            itemView.getContext().startActivity(startIntent);
        });
    }
}