package com.agoodidea.photoAlbum.filter;

import com.agoodidea.photoAlbum.model.entity.LoginUser;
import com.agoodidea.photoAlbum.utils.JwtUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * 认证拦截未携带令牌信息的请求
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final StringRedisTemplate stringRedisTemplate;
    private final Gson GSON = new Gson();

    public JwtAuthenticationTokenFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取请求头令牌信息
     * 未携带令牌信息放行，由后面的过滤器执行，
     * UsernamePasswordAuthenticationToken认证处理代码不执行，认证无法通过
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (StringUtils.hasText(token)) {
            Claims claims = JwtUtil.parseJwt(token);
            if (!ObjectUtils.isEmpty(claims)) {
                String userId = claims.getSubject();
                // 获取redis中的用户信息，redis信息为空时，未登录
                String loginUserJson = stringRedisTemplate.opsForValue().get("login:" + userId);
                // 注意：这里当登录成功后再次登录时，会携带token，此时不会走自定义登录逻辑
                LoginUser loginUser = GSON.fromJson(loginUserJson, LoginUser.class);
                if (loginUser != null) {
                    Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
