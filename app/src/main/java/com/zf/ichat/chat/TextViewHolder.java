package com.zf.ichat.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zf.ichat.ChatApplication;
import com.zf.ichat.R;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;

public class TextViewHolder extends RecyclerView.ViewHolder {
    private final ImageView avatarView;
    private final TextView messageView;
    private Contact self;
    private Conversation convr;

    public TextViewHolder(ViewGroup parent, Conversation convr) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text, parent, false));
        avatarView = itemView.findViewById(R.id.avatarView);
        messageView = itemView.findViewById(R.id.messageView);
        self = ((ChatApplication) parent.getContext().getApplicationContext()).getSelf();
        this.convr = convr;
    }

    public void bindTo(Message message) {
        int pL = messageView.getPaddingLeft();
        int pT = messageView.getPaddingTop();
        int pR = messageView.getPaddingRight();
        int pB = messageView.getPaddingBottom();
        if (message.isBelong()) {
            Glide.with(avatarView).load(convr.getAvatarUrl()).into(avatarView);
            messageView.setBackgroundResource(R.drawable.selector_bg_message);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            Glide.with(avatarView).load(self.getAvatarUrl()).into(avatarView);
            messageView.setBackgroundResource(R.drawable.selector_bg_my_message);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        messageView.setPadding(pL, pT, pR, pB);
        messageView.setText(message.getMessage());
    }
}