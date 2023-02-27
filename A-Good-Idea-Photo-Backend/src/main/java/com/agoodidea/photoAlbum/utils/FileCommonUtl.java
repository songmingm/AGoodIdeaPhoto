package com.agoodidea.photoAlbum.utils;

import cn.hutool.core.io.FileUtil;
import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.model.entity.PhotoMetaData;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.gson.Gson;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * 文件工具类
 */
public class FileCommonUtl {

    private final static Gson gson = new Gson();

    /**
     * 提取的元数据字段属性
     */
    private final static String[] metas = {
            "Image Width",
            "Image Height",
            "Make", "Made",
            "SoftWare", "Flash",
            "GPS Longitude",
            "GPS Latitude",
            "GPS Altitude",
            "Date/Time Original"
    };

    /**
     * 根据完整访问链接，截取文件名
     * eg: 协议://ip:port/bucketName/风铃女-1668848418290.jpg
     * get：风铃女-1668848418290.jpg
     */
    public static String splitFilename(String url) {
        if (url.contains("/")) {
            int lastIndexOf = url.lastIndexOf('/');
            return url.substring(lastIndexOf);
        }
        return url;
    }


    /**
     * 格式化文件的大小
     */
    public static String formatFileSize(Long fileLength) {
        String fileSizeString = "";
        if (fileLength == null) {
            return fileSizeString;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + "B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "K";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 获取时间戳(System.currentTimeMillis())，作为上传文件的文件名
     * eg:123119231231.jpg
     * 1.分割字符串，获取文件前缀名和后缀名
     * 2.形成新的文件名
     */
    public static String renameFile(String fileOriginalName) {
        String ext = FileUtil.extName(fileOriginalName);
        return System.currentTimeMillis() + "." + ext;
    }

    /**
     * 判断文件是否为图片
     */
    public static boolean isImage(String imageName) {
        String ext = FileUtil.extName(imageName);
        String[] imageExt = {"gif", "jpeg", "jpg", "png"};
        return Arrays.asList(imageExt).contains(ext);
    }

    /**
     * 压缩图片，统一压缩成350*200的缩略图，返回该文件的字节数组
     *
     * @param image  压缩文件
     * @param width  压缩图片宽度
     * @param height 压缩图片高度
     */
    public static byte[] compressImageToBytes(MultipartFile image, int width, int height) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Thumbnails.of(image.getInputStream()).size(width, height).toOutputStream(bos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    /**
     * 通过字节数组得到该文件的输入流
     */
    public static InputStream getImageInputSteam(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 解析文件流中的meta数据
     *
     * @param file 文件
     * @return 元数据实体
     */
    public static PhotoMetaData getImageMetaData(MultipartFile file) {
        try {
            PhotoMetaData data = PhotoMetaData.builder().build();
            HashMap<String, String> map = new HashMap<>();
            Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            // 获取所有目录
            Iterable<Directory> directories = metadata.getDirectories();
            for (Directory directory : directories) {
                Collection<Tag> tags = directory.getTags();
                for (Tag tag : tags) {
                    switch (tag.getTagName()) {
                        case "Image Width":
                            data.setWidth(tag.getDescription());break;
                        case "Image Height":
                            data.setHeight(tag.getDescription());break;
                        case "Make":
                            data.setMake(tag.getDescription());break;
                        case "Model":
                            data.setModel(tag.getDescription());break;
                        case "Software":
                            data.setSoftware(tag.getDescription());break;
                        case "Flash":
                            data.setFlash(tag.getDescription());break;
                        case "GPS Longitude":
                            data.setLongitude(tag.getDescription());break;
                        case "GPS Latitude":
                            data.setLatitude(tag.getDescription());break;
                        case "GPS Altitude":
                            data.setAltitude(tag.getDescription());break;
                        case "Date/Time Original":
                            data.setTime(tag.getDescription());break;
                    }
                    map.put(tag.getTagName(), tag.getDescription());
                }
            }
            return data;
        } catch (Exception e) {
            throw new BusinessException("解析图片元数据异常：" + e.getMessage());
        }
    }

}
