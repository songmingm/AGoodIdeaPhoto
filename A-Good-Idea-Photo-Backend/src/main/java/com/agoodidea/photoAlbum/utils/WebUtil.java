package com.agoodidea.photoAlbum.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * web工具类
 */
public class WebUtil {

    /**
     * http响应数据
     */
    public static <T> void httpRespJson(HttpServletResponse response, String json) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
