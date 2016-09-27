package com.strongman.splash.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.io.File;

/**
 * load image into imageview interface.
 * you can implement the interface to use custom imageload
 * Created by Strongman on 2016/9/27.
 */

public interface ImageLoader {

    void loadFromResId(Context context, @DrawableRes int resId, ImageView targetView, int placeHolderResId, int errorResId);

    void loadFromUrl(Context context, String url, ImageView targetView, int placeHolderResId, int errorResId);

    void loadFromFile(Context context, File file, ImageView targetView, int placeHolderResId, int errorResId);

}
