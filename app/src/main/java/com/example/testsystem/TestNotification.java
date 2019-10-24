package com.example.testsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import utils.NotificationPermissionUtil;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

public class TestNotification extends AppCompatActivity {

    private Button bt_set_notification_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);

        bt_set_notification_permission = findViewById(R.id.bt_set_notification);
        bt_set_notification_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationPermissionUtil.setSystemNotificationPermission(getApplicationContext());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotifySetting();
    }

    private void checkNotifySetting() {

        if (NotificationPermissionUtil.isOpen(getApplicationContext())) {
            bt_set_notification_permission.setText("通知权限已经被打开" +
                    "\n手机型号:" + android.os.Build.MODEL +
                    "\nSDK版本:" + android.os.Build.VERSION.SDK +
                    "\n系统版本:" + android.os.Build.VERSION.RELEASE +
                    "\n软件包名:" + getPackageName());

        } else {
            bt_set_notification_permission.setText("还没有开启通知权限，点击去开启");
        }
    }
}
