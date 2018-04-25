package com.zf.ichat.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zf.ichat.R;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;

public class TextViewHolder extends MessageViewHolder {
    private final TextView messageView;

    public TextViewHolder(ViewGroup parent, Conversation convr) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text, parent, false), convr);
        messageView = itemView.findViewById(R.id.messageView);
    }

    @Override
    public void bindTo(Message message) {
        super.bindTo(message);
        int pL = messageView.getPaddingLeft();
        int pT = messageView.getPaddingTop();
        int pR = messageView.getPaddingRight();
        int pB = messageView.getPaddingBottom();
        if (message.isBelong()) {
            Glide.with(avatarView).load(self.getAvatarUrl()).into(avatarView);
            messageView.setBackgroundResource(R.drawable.bg_chat_my_text);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            Glide.with(avatarView).load(convr.getAvatarUrl()).into(avatarView);
            messageView.setBackgroundResource(R.drawable.bg_chat_text);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        messageView.setPadding(pL, pT, pR, pB);
        messageView.setText(message.getMessage());
    }
}