package com.spring.boot.okhttp.service.impl;

import com.spring.boot.okhttp.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * <p>
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/22 16:43
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {
    /**
     * 有效
     *
     * @return
     */
    @Override
    public String getResourceFromResourceUtils() {
        File file;
        try {
            // 能读取系统中文件，不能读取jar包中文件
            file = ResourceUtils.getFile("D:/IdeaProjects/suyi-ecloud-20201119/ece-api-service/src/main/resources/client/ds.crt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "file not found";
        }

        try (InputStream inputStream = new FileInputStream(file);) {

            String resource = new String(inputStream.readAllBytes());
            log.debug(resource);
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }

    @Override
    public String getResourceFromClassPathResource() {
        ClassPathResource classPathResource = new ClassPathResource("client/ds.crt");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            String resource = new String(inputStream.readAllBytes());
            log.debug(resource);
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }

    @Override
    public String getResourceFromFileSystemResource() {
        FileSystemResource fileSystemResource = new FileSystemResource("D:/IdeaProjects-MyProject/rock-boot/spring-boot-okhttp/src/main/resources/client/ds.crt");
        try {
            InputStream inputStream = fileSystemResource.getInputStream();
            String resource = new String(inputStream.readAllBytes());
            log.debug(resource);
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }
}
