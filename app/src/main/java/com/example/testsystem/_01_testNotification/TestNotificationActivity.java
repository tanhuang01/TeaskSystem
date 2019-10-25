package com.example.testsystem._01_testNotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Button;

import com.example.testsystem.R;

import utils.NotificationPermissionUtil;

public class TestNotificationActivity extends AppCompatActivity {

    private Button bt_set_notification_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);

        bt_set_notification_permission = findViewById(R.id.bt_set_notification);
        bt_set_notification_permission.setOnClickListener(v ->
                NotificationPermissionUtil.setSystemNotificationPermission(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotifySetting();
    }

    private void checkNotifySetting() {

        if (NotificationPermissionUtil.isOpened(getApplicationContext())) {
            bt_set_notification_permission.setText("通知权限已经被打开" +
                    "\n手机型号:" + android.os.Build.MODEL +
                    "\nSDK版本:" + android.os.Build.VERSION.SDK +
                    "\n系统版本:" + android.os.Build.VERSION.RELEASE +
                    "\n软件包名:" + getPackageName());

        } else {
            bt_set_notification_permission.setText("还没有开启通知权限，点击去开启");


        }

        // 展示 权限申请对话框.
        NotificationPermissionUtil.tipIfHasNoPermission(this);

    }
}
