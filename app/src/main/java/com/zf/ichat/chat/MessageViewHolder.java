package com.zf.ichat.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zf.ichat.ChatApplication;
import com.zf.ichat.R;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;
import com.zf.ichat.util.ViewHolder;

public abstract class MessageViewHolder extends ViewHolder<Message> {
    final TextView timeView;
    final ImageView avatarView;
    Contact self;
    Conversation convr;

    public MessageViewHolder(View view, Conversation convr) {
        super(view);
        timeView = itemView.findViewById(R.id.timeView);
        avatarView = itemView.findViewById(R.id.avatarView);
        self = ((ChatApplication) itemView.getContext().getApplicationContext()).getSelf();
        this.convr = convr;
    }

    public void bindTo(Message message, boolean showTime) {
        if (showTime) {
            timeView.setText(String.valueOf(message.getCreateTime()));
            timeView.setVisibility(View.VISIBLE);
        } else {
            timeView.setVisibility(View.GONE);
        }
        bindTo(message);
    }
}