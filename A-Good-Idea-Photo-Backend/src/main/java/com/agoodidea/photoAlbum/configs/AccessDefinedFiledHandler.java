package com.agoodidea.photoAlbum.configs;

import com.agoodidea.photoAlbum.utils.ResponseCode;
import com.agoodidea.photoAlbum.utils.ResponseJsonData;
import com.agoodidea.photoAlbum.utils.WebUtil;
import com.google.gson.Gson;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败处理器
 */
@Component
public class AccessDefinedFiledHandler implements AccessDeniedHandler {

    private final static Gson GSON = new Gson();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseJsonData<String> responseJsonData = new ResponseJsonData<>();
        responseJsonData.setStatus(ResponseCode.AUTH_PRE_ERROR.getCode());
        responseJsonData.setMsg(ResponseCode.AUTH_PRE_ERROR.getMsg() + "：" + accessDeniedException.getMessage());
        WebUtil.httpRespJson(response, GSON.toJson(responseJsonData));
    }
}
