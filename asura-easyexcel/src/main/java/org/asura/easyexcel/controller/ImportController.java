package org.asura.easyexcel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.easyexcel.service.ImportDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导入功能API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportDataService importDataService;

    /**
     * http://localhost:8080/api/import/file
     *
     * 文件导入接口
     * @param file 上传的文件
     * @param userId 用户ID（从登录态获取，此处简化为参数）
     */
    @PostMapping("/file")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        try {
            importDataService.importFile(file, userId);
            return ResponseEntity.ok("文件导入成功");
        } catch (Exception e) {
            log.error("用户{}：导入接口异常", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("文件导入失败：" + e.getMessage());
        }
    }
}
