package com.strongman.splash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * this layout use for splash
 * Created by Strongman on 2016/9/26.
 */

public class SplashLayout extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

    /** background imageview */
    private ImageView mSplashImage;
    private View mCounterLayout;
    private TextView mRemainTimeTitleTv;
    private TextView mRemainTimeValueTv;

    private String mImageUrl;
    private File mImageFile;
    private int mImagePlaceHolderResId;
    private int mImageErrorResId;
    private int mImageResId;

    /**
     * is show remain time counter
     */
    private boolean isShowRemainCounter;
    /**
     * defualt remain time to navigate to other page
     */
    private int mTimeCount = 5;
    /**
     * the remain time
     */
    private int mTimeRemain;
    /**
     * the duration of the counter to decrease
     */
    private long mCounterDuration = 1000;

    private CounterListener mCounterListener;


    public interface CounterListener {
        void stop();
    }

    private Handler mh = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mRemainTimeValueTv.setText(String.valueOf(mTimeRemain));
            if(mTimeRemain <= 0) {
                Log.i("splashLayout", "mTimeCount <= 0");
                if(mCounterListener != null) {
                    mCounterListener.stop();
                }
            } else {
                mTimeRemain --;
                mh.postDelayed(mRunnable, mCounterDuration);
            }
        }
    };



    public SplashLayout(Context context) {
        this(context, null);
    }

    public SplashLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.splash_layout, this, true);

        this.mSplashImage = (ImageView) findViewById(R.id.splash_image);
        this.mCounterLayout = findViewById(R.id.splash_jump_over);
        this.mRemainTimeTitleTv = (TextView) findViewById(R.id.splash_jump_over_remain_time_title);
        this.mRemainTimeValueTv = (TextView) findViewById(R.id.splash_jump_over_remain_time);

        final TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SplashLayout,
                defStyleAttr,0);
        isShowRemainCounter = ta.getBoolean(R.styleable.SplashLayout_counter_is_show, false);
        mTimeRemain = mTimeCount = ta.getInt(R.styleable.SplashLayout_counter_time_total, mTimeCount);

        mImagePlaceHolderResId = ta.getResourceId(R.styleable.SplashLayout_splash_img_placeholder, 0);
        mImageErrorResId = ta.getResourceId(R.styleable.SplashLayout_splash_img_error, 0);

        if(isShowRemainCounter) {
            int counterTimeTextColor = ta.getColor(R.styleable.SplashLayout_counter_time_text_color, Color.rgb(0, 0, 0));
            float counterTimeTextSize = ta.getDimension(R.styleable.SplashLayout_counter_time_text_size, sp2px(context, 12));
            int counterPaddingTop = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_padding_top, dp2px(context, 5));
            int counterPaddingRight = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_padding_right, dp2px(context, 5));
            int counterPaddingBottom = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_padding_bottom, dp2px(context, 5));
            int counterPaddingLeft = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_padding_left, dp2px(context, 5));
            int counterTopMargin = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_margin_top, dp2px(context, 5));
            int counterRightMargin = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_margin_right, dp2px(context, 10));
            //int counterBackgroundColor = ta.getColor(R.styleable.SplashLayout_counter_time_background_color, Color.rgb(0, 0, 0));
            int counterLayoutBackgroundResId = ta.getResourceId(R.styleable.SplashLayout_counter_time_background_drawable, R.drawable.shape_splash_counter_background);
            int counterBetweenTitleAndNumMargin = ta.getDimensionPixelSize(R.styleable.SplashLayout_counter_time_between_title_and_num_margin, dp2px(context, 5));

            this.mCounterLayout.setOnClickListener(this);
            //set counter layout margink
            LayoutParams lp = (LayoutParams) this.mCounterLayout.getLayoutParams();
            lp.topMargin = counterTopMargin;
            lp.rightMargin = counterRightMargin;
            this.mCounterLayout.setLayoutParams(lp);
            //set counter layout padding
            this.mCounterLayout.setPadding(counterPaddingTop, counterPaddingRight, counterPaddingBottom, counterPaddingLeft);
            //set counter layout background drawable
            if(counterLayoutBackgroundResId != 0) {
                this.mCounterLayout.setBackgroundResource(counterLayoutBackgroundResId);
            }
            //set margin of counter between title and num
            LinearLayout.LayoutParams titleLp = (LinearLayout.LayoutParams) this.mRemainTimeTitleTv.getLayoutParams();
            titleLp.rightMargin = counterBetweenTitleAndNumMargin;
            this.mRemainTimeTitleTv.setLayoutParams(titleLp);
            //set counter time color and size
            this.mRemainTimeTitleTv.setTextColor(counterTimeTextColor);
            this.mRemainTimeTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, counterTimeTextSize);
            this.mRemainTimeValueTv.setTextColor(counterTimeTextColor);
            this.mRemainTimeValueTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, counterTimeTextSize);
        }

        ta.recycle();
    }



    public SplashLayout setSplashImage(String imageUrl) {
        if(mImageFile != null || mImageResId != 0){
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        this.mImageUrl = imageUrl;
        setupSplashImage();
        return this;
    }


    public SplashLayout setSplashImage(File imageFile) {
        if(mImageUrl != null || mImageResId != 0){
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        this.mImageFile = imageFile;
        setupSplashImage();
        return this;
    }


    public SplashLayout setSplashImage(@DrawableRes int resId) {
        if(mImageUrl != null || mImageFile != null){
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        this.mImageResId = resId;
        setupSplashImage();
        return this;
    }


    private void setupSplashImage() {
        if(mImageUrl != null) {
            Glide.with(mContext).load(mImageUrl)
                    .crossFade()
                    .placeholder(mImagePlaceHolderResId)
                    .error(mImageErrorResId)
                    .into(mSplashImage);
        } else if(mImageFile != null) {
            Glide.with(mContext).load(mImageFile)
                    .crossFade()
                    .placeholder(mImagePlaceHolderResId)
                    .error(mImageErrorResId)
                    .into(mSplashImage);
        } else if(mImageResId != 0) {
            Glide.with(mContext).load(mImageResId)
                    .crossFade()
                    .placeholder(mImagePlaceHolderResId)
                    .error(mImageErrorResId)
                    .into(mSplashImage);
        } else {
            throw new IllegalStateException("Please invoke setSplashImage method to set image for show");
        }
    }


    /**
     * start count
     */
    public void startCount() {
        if(mCounterLayout.getVisibility() != View.VISIBLE) {
            mCounterLayout.setVisibility(View.VISIBLE);
        }
        mTimeRemain = mTimeCount;
        mh.post(mRunnable);
    }


    /**
     * restart counter
     */
    public void restartCount() {
        if(mCounterLayout.getVisibility() != View.VISIBLE) {
            mCounterLayout.setVisibility(View.VISIBLE);
        }
        mTimeRemain = mTimeCount;
        mh.post(mRunnable);
    }


    /**
     * resume counter
     */
    public void resumeCount() {
        if(mCounterLayout.getVisibility() != View.VISIBLE) {
            mCounterLayout.setVisibility(View.VISIBLE);
        }
        if(mTimeCount > 0) {
            mh.post(mRunnable);
        }
    }

    /**
     * pause counter
     */
    public void pauseCount() {
        mh.removeCallbacks(mRunnable);
    }


    public void setCounterStop(CounterListener mCounterListener) {
        this.mCounterListener = mCounterListener;
    }


    @Override
    public void onClick(View v) {
        pauseCount();
        if(mCounterListener != null) {
            mCounterListener.stop();
        }
    }


    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



}
