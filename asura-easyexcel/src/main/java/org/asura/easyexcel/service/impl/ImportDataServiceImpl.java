package org.asura.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.asura.easyexcel.dto.ImportDataDTO;
import org.asura.easyexcel.listener.ImportDataListener;
import org.asura.easyexcel.service.ImportDataService;
import org.asura.easyexcel.service.SaveDataService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.UUID;

@Slf4j
@Service
public class ImportDataServiceImpl implements ImportDataService {

    // 根临时目录（建议配置在application.yml）
    @Value("${import.temp.dir:/tmp/import_temp}")
    private String rootTempDir;

    @Resource
    private SaveDataService saveDataService;

    /**
     * 统一导入入口
     *
     * @param file   上传的文件（Excel/ZIP）
     * @param userId 用户唯一标识（防止多用户冲突）
     */
    @Override
    public void importFile(MultipartFile file, String userId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名称不能为空");
        }

        // 获取文件后缀
        String fileExt = FilenameUtils.getExtension(originalFilename).toLowerCase();
        log.info("用户{}：开始导入文件，文件名：{}，文件类型：{}", userId, originalFilename, fileExt);

        try {
            switch (fileExt) {
                case "xls":
                case "xlsx":
                    // 直接处理Excel文件
                    log.info("Excel文件导入===============");
                    processExcelFile(file.getInputStream(), userId);
                    break;
                case "zip":
                    // 处理ZIP文件（先保存到用户临时目录，再解析内部Excel）
                    log.info("ZIP文件导入===============");
                    processZipFile(file, userId);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的文件类型：" + fileExt + "，仅支持.xls/.xlsx/.zip");
            }
        } catch (Exception e) {
            log.error("用户{}：文件导入失败，文件名：{}", userId, originalFilename, e);
            throw new RuntimeException("文件导入失败：" + e.getMessage());
        }
    }

    /**
     * 处理Excel文件（直接读取数据）
     */
    private void processExcelFile(InputStream inputStream, String userId) {
        // 创建监听器（绑定用户ID和业务服务）
//        ImportDataListener listener = new ImportDataListener(this, userId);
        ImportDataListener listener = new ImportDataListener(saveDataService, userId);
        // 使用EasyExcel读取Excel数据
        EasyExcel.read(inputStream, ImportDataDTO.class, listener)
                .sheet() // 读取第一个sheet
                .headRowNumber(1) // 表头行号（根据实际调整）
                .doRead();
    }

    /**
     * 处理ZIP文件（先保存到用户临时目录，再解压解析内部Excel）
     */
    private void processZipFile(MultipartFile file, String userId) throws Exception {
        // 1. 创建用户唯一临时目录（格式：根目录/用户ID/随机UUID）例：D:/import_temp\\user001\b2ec5262-b3c0-4b42-b0f2-9c58b849cb8d
        String userTempDir = rootTempDir + File.separator + userId + File.separator + UUID.randomUUID();
        Path userTempPath = Paths.get(userTempDir);
        // 确保所有父目录都被创建（修复核心：Files.createDirectories会创建所有不存在的父目录）
        Files.createDirectories(userTempPath);
        log.info("用户{}：创建临时目录：{}，目录是否存在：{}", userId, userTempDir, Files.exists(userTempPath));

        // 2. 保存ZIP文件到用户临时目录（修复：先校验目录，再用transferTo的Path重载，避免路径问题）
        String zipFileName = file.getOriginalFilename() == null ? "temp.zip" : file.getOriginalFilename();
        Path zipFilePath = userTempPath.resolve(zipFileName); // 用Path.resolve拼接路径，避免分隔符问题

        // 复制zip文件到指定路径
        // 核心修复：使用Path而非File的transferTo重载，更稳定
        file.transferTo(zipFilePath);
        log.info("用户{}：ZIP文件已保存到临时目录：{}，文件是否存在：{}", userId, zipFilePath, Files.exists(zipFilePath));

        try {
            // 3. 解压并读取ZIP中的Excel文件（原有逻辑不变）
            try (ZipFile zip = ZipFile.builder().setFile(zipFilePath.toString()).setCharset(StandardCharsets.UTF_8).get()) {
                Enumeration<ZipArchiveEntry> entries = zip.getEntries();
                while (entries.hasMoreElements()) {
                    ZipArchiveEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    String entryExt = FilenameUtils.getExtension(entryName).toLowerCase();

                    if (!entry.isDirectory() && (entryExt.equals("xls") || entryExt.equals("xlsx"))) {
                        log.info("用户{}：开始解析ZIP中的Excel文件：{}", userId, entryName);
                        try (InputStream inputStream = zip.getInputStream(entry)) {
                            processExcelFile(inputStream, userId);
                        }
                    }
                }
            }
        } finally {
            // 4. 清理临时目录（无论成功失败都清理）
            cleanTempDirectory(userTempPath, userId);
        }
    }

    /**
     * 清理临时目录（无论成功失败都执行）
     * @param userTempPath 用户随机临时目录路径
     * @param userId 用户ID
     */
    private void cleanTempDirectory(Path userTempPath, String userId) {
        try {
            // 第一步：删除随机UUID的临时目录
            if (Files.exists(userTempPath)) {
                FileUtils.deleteDirectory(userTempPath.toFile());
                log.info("用户{}：随机临时目录已删除：{}", userId, userTempPath);
            }

            // 第二步：删除用户级父目录（根目录/用户ID），仅当该目录为空时删除
            Path userParentDir = userTempPath.getParent();
            if (Files.exists(userParentDir) && Files.isDirectory(userParentDir)) {
                File userParentFile = userParentDir.toFile();
                File[] childFiles = userParentFile.listFiles();
                // 检查用户目录是否为空，避免删除其他用户/批次的目录
                boolean isEmpty = childFiles == null || childFiles.length == 0;
                if (isEmpty) {
                    FileUtils.deleteDirectory(userParentFile);
                    log.info("用户{}：空的用户级临时目录已删除：{}", userId, userParentDir);
                } else {
                    log.info("用户{}：用户级临时目录非空，暂不删除：{}", userId, userParentDir);
                }
            }
        } catch (Exception e) {
            // 捕获清理异常，避免影响主流程
            log.error("用户{}：临时目录清理失败", userId, e);
        }
        log.info("用户{}：临时目录清理流程执行完成，随机目录：{}", userId, userTempPath);
    }

}
