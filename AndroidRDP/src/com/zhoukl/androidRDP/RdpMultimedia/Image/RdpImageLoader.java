package com.zhoukl.androidRDP.RdpMultimedia.Image;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class RdpImageLoader {

    public static void displayImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    public static void displayImage(String uri, ImageView imageView, int defaultImgResId) {
        if (uri == null || uri.trim().isEmpty()) {
            imageView.setImageResource(defaultImgResId);
        } else {
            ImageLoader.getInstance().displayImage(uri, imageView);
        }
    }
    
}
