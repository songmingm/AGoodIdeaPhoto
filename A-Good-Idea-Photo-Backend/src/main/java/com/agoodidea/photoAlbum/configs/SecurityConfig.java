package com.agoodidea.photoAlbum.configs;

import com.agoodidea.photoAlbum.filter.EmailCodeAuthenticationProvider;
import com.agoodidea.photoAlbum.filter.JwtAuthenticationTokenFilter;
import com.agoodidea.photoAlbum.service.impl.LoginByAccountServiceImpl;
import com.agoodidea.photoAlbum.service.impl.LoginByEmailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFailedHandler authenticationFailedHandler;
    private final AccessDefinedFiledHandler accessDefinedFiledHandler;
    private final IgnoreUrlConfig ignoreUrlConfig;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final LoginByEmailServiceImpl loginByEmailService;
    private final LoginByAccountServiceImpl loginByAccountService;

    public SecurityConfig(AuthenticationFailedHandler authenticationFailedHandler,
                          AccessDefinedFiledHandler accessDefinedFiledHandler,
                          IgnoreUrlConfig ignoreUrlConfig,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          LoginByEmailServiceImpl loginByEmailService,
                          LoginByAccountServiceImpl loginByAccountService) {
        this.authenticationFailedHandler = authenticationFailedHandler;
        this.accessDefinedFiledHandler = accessDefinedFiledHandler;
        this.ignoreUrlConfig = ignoreUrlConfig;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.loginByEmailService = loginByEmailService;
        this.loginByAccountService = loginByAccountService;
    }

    /**
     * 主要配置
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        // 不需要保护的资源
        ignoreUrlConfig.getUrl().forEach(url -> registry.antMatchers(url).permitAll());
        // 允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS).permitAll();
        // 任何请求需要身份认证
        registry.and()
                .authorizeRequests().anyRequest().authenticated()
                // 关闭跨站请求防护及不使用session
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and().cors()
                .and()
                // 自定义权限拒绝处理类
                .exceptionHandling()
                .authenticationEntryPoint(authenticationFailedHandler)
                .accessDeniedHandler(accessDefinedFiledHandler)
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 账号密码认证
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(loginByAccountService);
        return daoAuthenticationProvider;
    }

    /**
     * 邮箱认证
     */
    @Bean
    public EmailCodeAuthenticationProvider emailCodeAuthenticationProvider() {
        return new EmailCodeAuthenticationProvider(loginByEmailService);
    }

    /**
     * 认证管理器，用于登陆认证
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider(), emailCodeAuthenticationProvider()));
    }

    /**
     * 设置默认的加密方式（强hash方式加密）
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 用于匹配url规则
     */
    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
