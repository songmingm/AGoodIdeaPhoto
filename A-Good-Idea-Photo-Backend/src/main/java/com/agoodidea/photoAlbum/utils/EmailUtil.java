package com.agoodidea.photoAlbum.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 邮件发送工具类
 */
@Component
public class EmailUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailUtil(JavaMailSender mailSender,
                     TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * 发送验证码
     *
     * @param subject 邮件主题
     * @param captcha 验证码字符数组
     * @param from    验证码发送人
     * @param to      收件人
     */
    public void sendCaptcha(String subject, char[] captcha, String from, String to) {
        Context context = new Context();
        context.setVariable("email", to);
        context.setVariable("verifyCode", captcha);
        String timePattern = "yyyy年MM月dd日 HH时mm分ss秒";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timePattern);
        String format = simpleDateFormat.format(new Date());
        context.setVariable("sendTime", format);
        String emailTemplate = "mail/captcha";
        String template = templateEngine.process(emailTemplate, context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(template, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("发送邮件错误：", e);
        }
    }
}
