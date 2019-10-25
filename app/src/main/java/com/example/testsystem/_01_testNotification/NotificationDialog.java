package com.example.testsystem._01_testNotification;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.testsystem.R;

import utils.NotificationPermissionUtil;

/**
 * 简介: 提示用户开启通知权限的的弹出框.
 *
 *
 */
public class NotificationDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "NotificationDialog";

    private  Button bt_set_notification;
    private  ImageView im_exit;
    private  CheckBox ch_no_long_prompt;

    // 标记为 true 时, 启动时不再显示该对话框
    // 用户点击 [不再显示] 只更改该标记位, 退出时在写入到外存中.
    private boolean isHide;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // 去掉 5.0 以上机器的默认白色背景
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bt_set_notification = view.findViewById(R.id.bt_set_notification);
        im_exit = view.findViewById(R.id.im_exit);
        ch_no_long_prompt = view.findViewById(R.id.ch_no_long_prompt);
        initListeners();
        super.onViewCreated(view, savedInstanceState);

    }

    private void initListeners() {
        bt_set_notification.setOnClickListener(this);
        im_exit.setOnClickListener(this);
        ch_no_long_prompt.setOnCheckedChangeListener((buttonView, isHide) -> {
            Log.i(TAG, "initListeners: " + buttonView + " isHide: " + isHide);
            this.isHide = isHide;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationPermissionUtil.setHide(getActivity(), isHide);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_exit:
                dismissAllowingStateLoss();
                break;
            case R.id.bt_set_notification:
                NotificationPermissionUtil.setSystemNotificationPermission(getActivity());
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
