package com.d.lib.pulllayout.edge.arrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.lib.pulllayout.R;
import com.d.lib.pulllayout.edge.EdgeView;
import com.d.lib.pulllayout.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeaderView extends EdgeView {

    private static final int ROTATE_ANIM_DURATION = 180;
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    private ImageView img_head_arrow;
    private LoadingView ldv_loading;
    private TextView tv_head_tip;
    private TextView tv_head_last_update_time;
    private RotateAnimation mRotateUpAnim, mRotateDownAnim;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lib_pull_edge_header;
    }

    @Override
    protected void init(@NonNull final Context context) {
        super.init(context);
        img_head_arrow = (ImageView) mContainer.findViewById(R.id.img_head_arrow);
        tv_head_tip = (TextView) mContainer.findViewById(R.id.tv_head_tip);
        ldv_loading = (LoadingView) mContainer.findViewById(R.id.ldv_loading);
        tv_head_last_update_time = (TextView) mContainer.findViewById(R.id.tv_head_last_update_time);
        mMeasuredHeight = Utils.dp2px(context, 60);
        initAnim();
        updateTime();
    }

    private void initAnim() {
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public void setState(int state) {
        if (mState == state) {
            return;
        }
        switch (state) {
            case STATE_NONE:
                img_head_arrow.setVisibility(View.VISIBLE);
                ldv_loading.setVisibility(View.INVISIBLE);
                if (mState == STATE_EXPANDED) {
                    img_head_arrow.clearAnimation();
                    img_head_arrow.startAnimation(mRotateDownAnim);
                } else if (mState == STATE_LOADING) {
                    img_head_arrow.clearAnimation();
                }
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_none));
                tv_head_tip.invalidate();
                tv_head_tip.requestLayout();
                break;

            case STATE_EXPANDED:
                img_head_arrow.setVisibility(View.VISIBLE);
                ldv_loading.setVisibility(View.INVISIBLE);
                img_head_arrow.clearAnimation();
                img_head_arrow.startAnimation(mRotateUpAnim);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_expanded));
                tv_head_tip.invalidate();
                tv_head_tip.requestLayout();
                break;

            case STATE_LOADING:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.INVISIBLE);
                ldv_loading.setVisibility(View.VISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_loading));
                anim(mMeasuredHeight, null);
                break;

            case STATE_SUCCESS:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.INVISIBLE);
                ldv_loading.setVisibility(View.INVISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_success));
                updateTime();
                reset();
                break;

            case STATE_ERROR:
                img_head_arrow.clearAnimation();
                img_head_arrow.setVisibility(View.INVISIBLE);
                ldv_loading.setVisibility(View.INVISIBLE);
                tv_head_tip.setText(getResources().getString(R.string.lib_pull_list_refresh_error));
                updateTime();
                reset();
                break;
        }
        mState = state;
    }

    @Override
    public void onPulled(float dx, float dy) {
        super.onPulled(dx, dy);
        Log.d("EdgeView", "mState: " + mState+" "+tv_head_tip.getText());

    }

    private void updateTime() {
        if (tv_head_last_update_time != null) {
            tv_head_last_update_time.setText(getResources().getString(R.string.lib_pull_list_header_last_time)
                    + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
        }
    }
}