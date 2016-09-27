package com.strongman.splash.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * the defult image loader use glide graph library
 * Created by Strongman on 2016/9/27.
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void loadFromResId(Context context, @DrawableRes int resId, ImageView targetView, int placeHolderResId, int errorResId) {
        Glide.with(context).load(resId)
                .placeholder(placeHolderResId)
                .error(errorResId)
                .into(targetView);
    }

    @Override
    public void loadFromUrl(Context context, String url, ImageView targetView, int placeHolderResId, int errorResId) {
        Glide.with(context).load(url)
                .placeholder(placeHolderResId)
                .error(errorResId)
                .into(targetView);
    }

    @Override
    public void loadFromFile(Context context, File file, ImageView targetView, int placeHolderResId, int errorResId) {
        Glide.with(context).load(file)
                .placeholder(placeHolderResId)
                .error(errorResId)
                .into(targetView);
    }
}
