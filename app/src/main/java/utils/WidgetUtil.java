package utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 简介:
 */
public class WidgetUtil {

    /**
     *
     * @param dp
     * @return dp 在当前设备性对应的 px 值
     */
    public static int dpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    /**
     *
     * @param dp
     * @return dp 在当前设备性对应的 px 值
     */
    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }


}
