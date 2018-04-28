package com.zf.ichat.chat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.zf.ichat.R;
import com.zf.ichat.data.Message;

import java.util.List;

public class ImageBrowseAdapter extends PagerAdapter implements OnPhotoTapListener, OnOutsidePhotoTapListener {
    private List<Message> messages = null;

    @Override
    public int getCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_browse, null);
        PhotoView photoView = view.findViewById(R.id.photoView);
        photoView.setOnPhotoTapListener(this);
        photoView.setOnOutsidePhotoTapListener(this);
        Glide.with(photoView).load(messages.get(position).getMessage()).into(photoView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        ((Activity) imageView.getContext()).finish();
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((Activity) view.getContext()).finish();
    }
}