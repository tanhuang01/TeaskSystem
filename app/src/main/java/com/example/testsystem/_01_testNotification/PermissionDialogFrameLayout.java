package com.example.testsystem._01_testNotification;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testsystem.R;

import utils.NotificationPermissionUtil;

/**
 * 简介: 权限申请界面的专用根布局.
 * 处理控件初始化和监听事件等相关逻辑. 避免这些逻辑在 Activity 中书写.
 */
public class PermissionDialogFrameLayout extends FrameLayout {

    private ImageView im_exit;
    private Button bt_set_notification;
    private CheckBox ch_no_long_prompt;

    private Boolean isHide = null;

    public PermissionDialogFrameLayout(@NonNull Context context) {
        super(context);
    }

    public PermissionDialogFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PermissionDialogFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        im_exit = findViewById(R.id.im_exit);
        bt_set_notification = findViewById(R.id.bt_set_notification);
        ch_no_long_prompt = findViewById(R.id.ch_no_long_prompt);

        im_exit.setOnClickListener(v -> {
            this.setVisibility(View.GONE);
        });

        ch_no_long_prompt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isHide = isChecked;
        });

        bt_set_notification.setOnClickListener(v -> {
            NotificationPermissionUtil.setSystemNotificationPermission(((Activity) getContext()));
            this.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.GONE && isHide != null && isHide) {
            NotificationPermissionUtil.setHide(getContext(), isHide);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
