package com.agoodidea.photoAlbum.controller;

import com.agoodidea.photoAlbum.mapper.FileMapper;
import com.agoodidea.photoAlbum.service.FileService;
import io.minio.MinioClient;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("file")
public class FileController {

    private final FileService fileService;
    private final FileMapper fileMapper;
    private final MinioClient minioClient;

    public FileController(FileService fileService,
                          FileMapper fileMapper,
                          MinioClient minioClient) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
        this.minioClient = minioClient;
    }

    @PostMapping("/yasuo")
    private void yasuo(MultipartFile file) {
        try {
            File newFile = new File("test.jpg");
            Thumbnails.of(file.getInputStream())
                    .size(300, 200)
                    .outputQuality(1.0f)
                    .keepAspectRatio(true)// 是否按照比例
                    .toFile(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
