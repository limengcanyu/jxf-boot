package org.asura.code.executor.controller;

import org.asura.code.executor.dto.ResultDTO;
import org.asura.code.executor.util.ClassNameResolver;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Java代码运行控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/java/execute")
public class JavaCodeExecuteController {

    @Resource
    private ClassNameResolver classNameResolver;

    /**
     * 文件上传运行
     */
    @PostMapping("/upload")
    public ResultDTO<String> runByFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        try {
            // 1. 文件基础校验
            if (file.isEmpty()) {
                return ResultDTO.fail("上传文件不能为空");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.endsWith(".java")) {
                return ResultDTO.fail("仅支持.java文件上传");
            }

            // 2. 读取文件内容
            String javaCode = readFileContent(file);
            String className = classNameResolver.resolveFromFileName(originalFilename);


            // 6. 返回结果
            return ResultDTO.success("");

        } catch (Exception e) {
            log.error("文件上传执行失败", e);
            return ResultDTO.fail(e.getMessage());
        }
    }

    /**
     * 文本提交运行
     */
    @PostMapping("/code")
    public ResultDTO<String> runByCode(
            @RequestParam("javaCode") String javaCode,
            @RequestParam(value = "className", required = false) String className,
            HttpServletRequest request) {
        try {
            // 1. 参数校验
            if (StringUtils.isBlank(javaCode)) {
                return ResultDTO.fail("Java源码不能为空");
            }

            // 2. 解析类名
            if (StringUtils.isBlank(className)) {
                className = classNameResolver.resolveClassName(javaCode);
            }

            // 6. 返回结果
            return ResultDTO.success("");

        } catch (Exception e) {
            log.error("文本提交执行失败", e);
            return ResultDTO.fail(e.getMessage());
        }
    }

    /**
     * 读取文件内容
     */
    private String readFileContent(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}
