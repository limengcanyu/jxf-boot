package com.spring.boot.okhttp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 19:19
 */
@SpringBootTest
public class FileTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void classPathResource() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("client/ds.crt");
        InputStream inputStream = classPathResource.getInputStream();
        System.out.println(new String(inputStream.readAllBytes()));
    }

    @Test
    void fileSystemResource() throws IOException {
        FileSystemResource fileSystemResource = new FileSystemResource("D:/IdeaProjects-MyProject/rock-boot/spring-boot-okhttp/src/main/resources/client/ds.crt");
        InputStream inputStream = fileSystemResource.getInputStream();
        System.out.println(new String(inputStream.readAllBytes()));
    }

    @Test
    void inputStreamResource() throws IOException {
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream("D:/IdeaProjects-MyProject/rock-boot/spring-boot-okhttp/src/main/resources/client/ds.crt"));
        InputStream inputStream = inputStreamResource.getInputStream();
        System.out.println(new String(inputStream.readAllBytes()));
    }

    @Test
    void applicationContext() throws IOException {
        Resource resource = applicationContext.getResource("classpath:client/ds.crt");
//        Resource resource = applicationContext.getResource("file:D:/IdeaProjects-MyProject/rock-boot/spring-boot-okhttp/src/main/resources/client/ds.crt");
        InputStream inputStream = resource.getInputStream();
        System.out.println(new String(inputStream.readAllBytes()));
    }

    @Test
    void getFile() throws IOException {
        File file = ResourceUtils.getFile("classpath:client/ds.crt");
        InputStream inputStream = new FileInputStream(file);
        System.out.println(new String(inputStream.readAllBytes()));
    }
}
