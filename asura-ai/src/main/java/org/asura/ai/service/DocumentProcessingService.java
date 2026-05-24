package org.asura.ai.service;

import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档处理服务接口
 * 提供文档解析、处理和存储功能
 */
public interface DocumentProcessingService {

    /**
     * 处理上传的文件，提取文档内容
     * @param file 上传的文件
     * @return 处理后的文档列表
     */
    List<Document> processFile(MultipartFile file);

    /**
     * 处理文件并存储到向量存储中
     * @param file 上传的文件
     */
    void processAndStore(MultipartFile file);
}