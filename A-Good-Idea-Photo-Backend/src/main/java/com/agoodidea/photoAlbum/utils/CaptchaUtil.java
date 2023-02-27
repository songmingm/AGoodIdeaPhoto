package com.agoodidea.photoAlbum.utils;

import java.util.Random;

/**
 * 验证码工具
 */
public class CaptchaUtil {

    /**
     * 验证码生成
     *
     * @return 验证码串
     */
    public static String builderCaptcha() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int codeLength = 6;
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < codeLength; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return (sb.toString());
    }

    /**
     * 不区分大小写匹配验证码
     */
    public static Boolean matchingCaptcha(String sourceCode, String targetCode) {
        String sourceCodeToUpperCase = sourceCode.toUpperCase();
        String targetCodeToUpperCase = targetCode.toUpperCase();
        return sourceCodeToUpperCase.equals(targetCodeToUpperCase);
    }


}
