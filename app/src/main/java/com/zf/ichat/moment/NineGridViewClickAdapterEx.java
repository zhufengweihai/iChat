package com.zf.ichat.moment;

import android.content.Context;
import android.widget.ImageView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridViewWrapper;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.List;

public class NineGridViewClickAdapterEx extends NineGridViewClickAdapter {
    public NineGridViewClickAdapterEx(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
    }

    protected ImageView generateImageView(Context context) {
        NineGridViewWrapper imageView = new NineGridViewWrapper(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }
}
