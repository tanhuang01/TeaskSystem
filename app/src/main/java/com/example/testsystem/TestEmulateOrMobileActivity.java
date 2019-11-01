package com.example.testsystem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.lahm.library.EasyProtectorLib;

/**
 * 简介: 测试当前运行的设备是相关参数, 判断是否为模拟器或者真机.
 */
public class TestEmulateOrMobileActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_emulate_or_mobile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE
            }, 2);
        } else {
            showEmulateInfo();
        }
    }

    private void showEmulateInfo() {
        // 获取当前设备的参数
        // 1. IMEI-移动设备国际身份码 和 IMSI-国际设备用户识别码.

        final TelephonyManager tm = ((TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
        String imei = tm.getDeviceId();
        String imsi = tm.getSubscriberId();

        // serial
        String serial = Build.SERIAL;
//        String serial_ = Build.getSerial();

        // mac 地址
        WifiManager wifiManager = ((WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE));
        WifiInfo info = wifiManager == null ? null : wifiManager.getConnectionInfo();
        String mac = info == null ? null : info.getMacAddress();


        // 光传感器 && 重力传感器
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);


        // 默认号码
        String line1Number = tm.getLine1Number();

        // Build 相关属性
        String BOARD = android.os.Build.BOARD; // The name of the underlying board, like "unknown".
        // This appears to occur often on real hardware... that's sad
//        String BOOTLOADER = android.os.Build.BOOTLOADER; // The system bootloader version number.
        String BRAND = android.os.Build.BRAND; // The brand (e.g., carrier) the software is customized for, if any.
        // "generic"
        String DEVICE = android.os.Build.DEVICE; // The name of the industrial design. "generic"
        String HARDWARE = android.os.Build.HARDWARE; // The name of the hardware (from the kernel command line or
        // /proc). "goldfish"
        String MODEL = android.os.Build.MODEL; // The end-user-visible name for the end product. "sdk"
        String PRODUCT = android.os.Build.PRODUCT; // The name of the overall product.
        if ((BOARD.compareTo("unknown") == 0) /* || (BOOTLOADER.compareTo("unknown") == 0) */
                || (BRAND.compareTo("generic") == 0) || (DEVICE.compareTo("generic") == 0)
                || (MODEL.compareTo("sdk") == 0) || (PRODUCT.compareTo("sdk") == 0)
                || (HARDWARE.compareTo("goldfish") == 0)) {
        }

        String builder = "imei: " + imei + "\n" +
                "imsi: " + imsi + "\n" +
                "serial: " + serial + "\n" +
                "mac: " + mac + "\n" +
                "light_sensor: " + (lightSensor == null ? "null" : lightSensor.getName()) + "\n" +
                "gravity_sensor: " + (gravitySensor == null ? "null" : gravitySensor.getName()) + "\n" +
                "line1Number: " + line1Number + "\n";

        String builder_build = "Build info --------------- \n" +
                "broad: " + BOARD +
                "\nbrand: " + BRAND +
                "\ndevice: " + DEVICE +
                "\nhardware: " + HARDWARE +
                "\nmodel: " + MODEL +
                "\nproduct: " + PRODUCT + "\n";

        StringBuilder builder_easyProtector = new StringBuilder("Protector------------\n");

        TextView tv_emulate_or_mobile_info =
                findViewById(R.id.tv_emulate_or_mobile_info);

        // 使用第三方的类库. // http://github.com/lamster2018/EasyProtector
        boolean isEmulator = EasyProtectorLib.checkIsRunningInEmulator(this,
                builder_easyProtector::append);
        builder_easyProtector.append("\nisEmulator: ").append(isEmulator);


        // 打印相关输出结果
        tv_emulate_or_mobile_info.setText(builder);
        tv_emulate_or_mobile_info.append(builder_build);
        tv_emulate_or_mobile_info.append(builder_easyProtector.toString());
        tv_emulate_or_mobile_info.append("\nisEmulator_self: " + isEmulator(this));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
            } else {
                showEmulateInfo();
            }
        }

    }

    public static boolean isEmulator(Context context) {
        // 光传感器.
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sm == null) {
            return true;
        }
        Sensor lightSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null || lightSensor.getName().equalsIgnoreCase("light sensor")) {
            // 没有光传感器为模拟器. 雷电模拟器有默认值 Light Sensor
            return true;
        }
        //
        // Build 相关类
        final String BOARD = Build.BOARD;
        final String BRAND = Build.BRAND;
        final String DEVICE = Build.DEVICE;
        final String HARDWARE = Build.HARDWARE;
        final String MODEL = Build.MODEL;
        final String PRODUCT = Build.PRODUCT;
        if ((BOARD.equalsIgnoreCase("unknown") ||
                BRAND.equalsIgnoreCase("generic") ||
                DEVICE.equalsIgnoreCase("generic") ||
                HARDWARE.equalsIgnoreCase("goldfish") ||
                MODEL.equalsIgnoreCase("sdk") ||
                PRODUCT.equalsIgnoreCase("sdk"))) {
            return true;
        }
        return false;
    }
}
