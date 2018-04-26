package com.zf.ichat.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zf.ichat.R;
import com.zf.ichat.data.Message;
import com.zf.ichat.util.ViewHolder;

import uk.co.senab.photoview.PhotoView;

public class ImageBrowseViewHolder extends ViewHolder<Message> {
    private final PhotoView photoView;

    public ImageBrowseViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_browse, parent, false));
        photoView = itemView.findViewById(R.id.photoView);
    }

    @Override
    public void bindTo(Message message) {
        Glide.with(photoView).load(message.getMessage()).into(photoView);
    }

    @Override
    public void onViewRecycled() {
        Glide.with(photoView).clear(photoView);
    }
}