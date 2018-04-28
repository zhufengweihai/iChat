package com.zf.ichat.chat;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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

public class ImageViewHolder extends MessageViewHolder {
    private final ImageView avatarView;
    private final ImageView imageView;
    private Contact self;
    private Conversation convr;

    public ImageViewHolder(ViewGroup parent, Conversation convr) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image, parent, false), convr);
        avatarView = itemView.findViewById(R.id.avatarView);
        imageView = itemView.findViewById(R.id.imageView);

        self = ((ChatApplication) parent.getContext().getApplicationContext()).getSelf();
        this.convr = convr;
    }

    @Override
    public void bindTo(Message message) {
        super.bindTo(message);
        if (message.isBelong()) {
            Glide.with(avatarView).load(self.getAvatarUrl()).into(avatarView);
            imageView.setScaleType(ImageView.ScaleType.FIT_END);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            Glide.with(avatarView).load(convr.getAvatarUrl()).into(avatarView);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        Glide.with(imageView).load(message.getMessage()).into(imageView);
        imageView.setOnClickListener(v -> {
            Intent startIntent = new Intent(itemView.getContext(), ImageBrowseActivity.class);
            startIntent.putExtra(ImageBrowseActivity.INTENT, convr.getContactId());
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(imageView, imageView.getWidth()
                    / 2, imageView.getHeight() / 2, imageView.getWidth(), imageView.getHeight());
            ActivityCompat.startActivity(itemView.getContext(), startIntent, compat.toBundle());
        });
    }

    @Override
    public void onViewRecycled() {
        Glide.with(imageView).clear(imageView);
    }
}