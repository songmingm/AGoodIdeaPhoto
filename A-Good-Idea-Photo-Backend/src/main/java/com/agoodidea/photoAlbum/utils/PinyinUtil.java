package com.agoodidea.photoAlbum.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PinyinUtil {

    private static final Logger logger = LoggerFactory.getLogger(PinyinUtil.class);

    /**
     * 获取中文串的第一个字母串
     * eg:华硕壁纸 -> hsbz
     */
    public static String getFirstSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        String trimChinese = chinese.trim();
        char[] chars = trimChinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : chars) {
            if ((int) c < 128) {
                pybf.append(c);
            } else {
                try {
                    String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    pybf.append(pinyin[0].charAt(0));
                } catch (Exception e) {
                    logger.info("拼音转换异常：" + e.getMessage());
                }
            }
        }
        return pybf.toString();
    }
}
