package com.agoodidea.photoAlbum;

import com.agoodidea.photoAlbum.mapper.FileMapper;
import com.agoodidea.photoAlbum.model.entity.PhotoMetaData;
import com.agoodidea.photoAlbum.utils.CaptchaUtil;
import com.agoodidea.photoAlbum.utils.GPSUtil;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
class AGoodIdeaPhotoAlbumBackendApplicationTests {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FileMapper fileMapper;

    @Test
    void contextLoads() throws MessagingException {
        String captcha = CaptchaUtil.builderCaptcha();
        char[] chars = captcha.toCharArray();
        Context context = new Context();
        context.setVariable("email", "1299463902@qq.com");
        context.setVariable("verifyCode", chars);
        String pattern = "yyyy年MM月dd日 HH时mm分ss秒";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String format = simpleDateFormat.format(new Date());
        context.setVariable("sendTime", format);
        String emailTemplate = "mail/register";
        String template = templateEngine.process(emailTemplate, context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("1299463902@qq.com");
        helper.setTo("1299463902@qq.com");
        helper.setText(template, true);
        javaMailSender.send(mimeMessage);

    }

    /**
     * 提取照片元数据测试
     */
    @Test
    void imageMetaData() throws ImageProcessingException, IOException {
        HashMap<String, String> map = new HashMap<>();
        File file = new File("D:\\document\\OneDrive\\桌面\\01.jpg");
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        // 获取所有目录
        Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
            Collection<Tag> tags = directory.getTags();
            for (Tag tag : tags) {
                map.put(tag.getTagName(), tag.getDescription());
            }
        }
        System.out.println(map);
        System.out.println("制造商：" + map.get("Make"));
        System.out.println("型号：" + map.get("Model"));
        System.out.println("经度：" + map.get("GPSLongitude"));
        System.out.println("纬度：" + map.get("GPSLatitude"));

        Gson gson = new Gson();

        String json = gson.toJson(map);
        PhotoMetaData photoMetaData = gson.fromJson(json, PhotoMetaData.class);
        System.out.println(photoMetaData);
    }

    @Test
    void meteDataTest2() throws ImageProcessingException, IOException {
        File file = new File("D:\\document\\OneDrive\\桌面\\01.jpg");
        Iterable<Directory> directories = ImageMetadataReader.readMetadata(file).getDirectories();
        PhotoMetaData data = PhotoMetaData.builder().build();
        for (Directory directory : directories) {
            Collection<Tag> tags = directory.getTags();
            for (Tag tag : tags) {
                switch (tag.getTagName()) {
                    case "Image Width":
                        data.setWidth(tag.getDescription());
                }
            }
        }
        System.out.println(data);
    }

    @Test
    void GPSUtil() {
        Double d1 = GPSUtil.dms2d("115° 51' 50.26\"");
        Double d2 = GPSUtil.dms2d("28° 42' 11.91\"");
        System.out.println(d1);
        System.out.println(d2);
    }

    @Test
    void minoMeteData() {
        String bucket = "agood-cszy-1671619153187";
        String object = "1671622649338.jpg";
        Map<String, String> metaData = fileMapper.getMetaData(bucket, object);
        System.out.println(metaData);
    }
}
