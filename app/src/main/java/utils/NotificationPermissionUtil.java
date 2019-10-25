package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.testsystem.R;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

/**
 * 简介: Google 对于不同的 Android 系统的获取权限的方式不同.
 * 所以相关逻辑进行抽取
 */
public class NotificationPermissionUtil {

    private static final String SHARED_CACHE = "CACHE";

    private static final String KEY_IS_HIDE = "KEY_IS_HIDE";

    private static final String NOTIFICATION_PERMISSION_DIALOG = "notification_permission_dialog";


    /**
     * 判断通知权限是否开启
     *
     * @param context
     * @return true-已开启系统通知权限. false-未开启
     */
    public static boolean isOpened(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，
        // 低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        return manager.areNotificationsEnabled();
    }


    /**
     * 跳转到系统的设置界面.
     *
     * @param context 因为要启动系统界面, 所以需要是一个 Activity.
     */
    public static void setSystemNotificationPermission(Activity context) {
        if (context == null) {
            return;
        }
        try {
            // 打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            // API 26 以上, 即 8.0（含8.0）
            intent.putExtra(EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);

            // API21——25，即 5.0——7.1
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);

            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();

            // 直接跳转到当前应用的设置界面。
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }

    /**
     * 判断是否要展示申请权限对话框.
     *
     * 仅仅当用户没有开启权限, 并且未选择 [不再提示] 的前提下. 才会展示申请权限的对话框.
     *
     * @return true - 需要展示申请权限对话框
     */
    public static boolean isShowNotificationTip(Activity context) {
        SharedPreferences pre =
                context.getSharedPreferences(SHARED_CACHE, Context.MODE_PRIVATE);
        boolean isHide = pre.getBoolean(KEY_IS_HIDE, false);
        // 未选择 [不再提示] && 没有通知权限
        return !isHide && !isOpened(context);
    }

    /***
     * 设置下一次启动主界面时, 申请权限对话框的显隐
     *
     * @param isHide true-下次启动时, 如果应用没有获取权限, 那么还会再次展示申请权限的对话框
     */
    public static void setHide(Context context, boolean isHide) {
        if (context != null && isHide) { // 用户选择了 [不再显示]
            SharedPreferences pre = context.getSharedPreferences(SHARED_CACHE, Context.MODE_PRIVATE);
            pre.edit()
                    .putBoolean(KEY_IS_HIDE, true)
                    .apply();
        }
    }


    /**
     * 如果未获得权限则弹出权限对话框, 否则不做操作.
     *
     * @param context
     */
    public static void tipIfHasNoPermission(FragmentActivity context) {
        // 暂时不采取使用 dialog 的这种方式
//        if (isShowNotificationTip(context)) {
//            NotificationDialog dialog = new NotificationDialog();
//            dialog.show(context.getSupportFragmentManager(), NOTIFICATION_PERMISSION_DIALOG);
//        }

        View view = context.findViewById(R.id.layout_notification_dialog);
        if (isShowNotificationTip(context)) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private static String NotificationTip(Context context) {
        if (isOpened(context)) {
            return "通知权限已经被打开" +
                    "\n手机型号:" + android.os.Build.MODEL +
                    "\nSDK版本:" + android.os.Build.VERSION.SDK +
                    "\n系统版本:" + android.os.Build.VERSION.RELEASE +
                    "\n软件包名:" + context.getPackageName();
        } else {
            return "还没有开启通知权限，点击去开启";
        }

    }


}
