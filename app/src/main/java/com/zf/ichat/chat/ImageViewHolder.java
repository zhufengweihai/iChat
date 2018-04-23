package com.zf.ichat.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zf.ichat.ChatApplication;
import com.zf.ichat.R;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;
import com.zf.ichat.util.ViewHolder;

public class ImageViewHolder extends ViewHolder<Message> {
    private final ImageView avatarView;
    private final ImageView imageView;
    private Contact self;
    private Conversation convr;

    public ImageViewHolder(ViewGroup parent, Conversation convr) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image, parent, false));
        avatarView = itemView.findViewById(R.id.avatarView);
        imageView = itemView.findViewById(R.id.imageView);
        self = ((ChatApplication) parent.getContext().getApplicationContext()).getSelf();
        this.convr = convr;
    }

    @Override
    public void bindTo(Message message) {
        int pL = imageView.getPaddingLeft();
        int pT = imageView.getPaddingTop();
        int pR = imageView.getPaddingRight();
        int pB = imageView.getPaddingBottom();
        Glide.with(imageView).load(message.getMessage()).into(imageView);
        if (message.isBelong()) {
            Glide.with(avatarView).load(convr.getAvatarUrl()).into(avatarView);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            Glide.with(avatarView).load(self.getAvatarUrl()).into(avatarView);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        //imageView.setPadding(pL, pT, pR, pB);
    }
}