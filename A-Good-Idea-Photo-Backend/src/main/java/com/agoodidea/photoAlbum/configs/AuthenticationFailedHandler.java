package com.agoodidea.photoAlbum.configs;

import com.agoodidea.photoAlbum.utils.ConstantsMsg;
import com.agoodidea.photoAlbum.utils.ResponseJsonData;
import com.agoodidea.photoAlbum.utils.WebUtil;
import com.google.gson.Gson;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.agoodidea.photoAlbum.utils.ResponseCode.USER_AUTH_ERROR;

/**
 * 认证失败处理器
 */
@Component
public class AuthenticationFailedHandler implements AuthenticationEntryPoint {

    private final static Gson GSON = new Gson();

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        ResponseJsonData<String> jsonData;
        if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
            jsonData = ResponseJsonData.failed("密码错误，"+e.getMessage());
        } else if (e instanceof LockedException) {
            jsonData = ResponseJsonData.failed("账户被锁定"+e.getMessage());
        } else if (e instanceof CredentialsExpiredException) {
            jsonData = ResponseJsonData.failed("密码过期"+e.getMessage());
        } else if (e instanceof DisabledException) {
            // 账户禁用
            jsonData = ResponseJsonData.failed(ConstantsMsg.User.ADMIN_STATUS_ERROR);
        } else {
            jsonData = ResponseJsonData.failed(USER_AUTH_ERROR);
        }
        WebUtil.httpRespJson(httpServletResponse, GSON.toJson(jsonData));
    }
}
