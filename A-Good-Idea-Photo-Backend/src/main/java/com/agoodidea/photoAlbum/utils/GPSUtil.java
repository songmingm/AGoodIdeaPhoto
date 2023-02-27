package com.agoodidea.photoAlbum.utils;

/**
 * GPS 位置信息转换
 * Create by mmsong on 2022/12/23 17:19
 */
public class GPSUtil {

    /**
     * 将度分秒转换为度
     * eg:115° 51' 50.26"
     *
     * @param locations 六十进制格式
     * @return 十进制度格式
     */
    public static Double dms2d(String locations) {
        //所有空格替换为""
        locations = locations.replace(" ", "");
        //按“°”符号分割字符串
        String[] str = locations.split("°");
        //第一部分为“度”
        int d = Integer.parseInt(str[0]);
        String[] str1 = str[1].split("'");
        int m = Integer.parseInt(str1[0]);
        //这里不应包含最后的“ " ”(秒的单位符号)
        double s = Double.parseDouble(str1[1].substring(0, str1[1].length() - 1));
        double min = m + (s / 60);
        return (min / 60) + Math.abs(d);
    }
}