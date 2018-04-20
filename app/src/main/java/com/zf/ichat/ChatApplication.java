package com.zf.ichat;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;
import com.zf.ichat.data.Contact;

public class ChatApplication extends Application {
    private Contact self;
    @Override
    public void onCreate() {
        super.onCreate();
        NineGridView.setImageLoader(new GlideImageLoader());
    }

    public Contact getSelf() {
        return self;
    }

    public void setSelf(Contact self) {
        this.self = self;
    }

    /**
     * Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url).into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
