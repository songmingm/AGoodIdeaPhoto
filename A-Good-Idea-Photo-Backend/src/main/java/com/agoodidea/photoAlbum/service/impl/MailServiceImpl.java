package com.agoodidea.photoAlbum.service.impl;

import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.service.MailService;
import com.agoodidea.photoAlbum.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements MailService {
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailUtil emailUtil;
    @Value("${spring.mail.username}")
    private String sender;

    public MailServiceImpl(StringRedisTemplate stringRedisTemplate,
                           EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Boolean sendEmailCode(String to, String code) {
        // 先判断这个邮箱是否已经存在未使用的验证码
        String hasCode = stringRedisTemplate.opsForValue().get(to);
        if (!ObjectUtils.isEmpty(hasCode)) {
            throw new BusinessException("验证码获取频繁，请稍后再试");
        }
        emailUtil.sendCaptcha("A GOOD IDEA 验证码", code.toCharArray(), sender, to);
        stringRedisTemplate.opsForValue().set(to, code, 5, TimeUnit.MINUTES);
        return true;
    }
}
