package com.agoodidea.photoAlbum.filter;

import com.agoodidea.photoAlbum.service.LoginByEmailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    private final LoginByEmailService loginService;

    public EmailCodeAuthenticationProvider(LoginByEmailService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        EmailCodeAuthenticationToken emailToken = (EmailCodeAuthenticationToken) authentication;
        String email = (String) emailToken.getPrincipal();
        UserDetails user = loginService.loginByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(user, user.getAuthorities());
        result.setDetails(emailToken.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
