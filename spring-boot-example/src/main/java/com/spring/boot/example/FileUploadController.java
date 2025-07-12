package com.spring.boot.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class FileUploadController {

    // 从配置文件中获取文件存储目录
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择要上传的文件");
        }

        try {
            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            // 检查上传目录是否存在，不存在则创建
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 构建文件存储路径
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath);

            return ResponseEntity.ok("文件上传成功，文件名: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
        }
    }
}
