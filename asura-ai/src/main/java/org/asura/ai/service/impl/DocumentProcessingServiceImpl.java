package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.service.DocumentProcessingService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档处理服务实现类
 * 支持PDF、Office文档和文本文件的解析处理
 */
@Service
public class DocumentProcessingServiceImpl implements DocumentProcessingService {

    @Resource
    private VectorStore vectorStore;

    /**
     * 处理上传的文件，根据文件类型选择相应的解析器
     * @param file 上传的文件
     * @return 处理后的文档列表
     */
    @Override
    public List<Document> processFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("File name is null");
        }

        String extension = getFileExtension(filename).toLowerCase();

        try {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes());

            return switch (extension) {
                case "pdf" -> processPdfFile(resource, filename);
                case "doc", "docx", "xls", "xlsx", "ppt", "pptx" -> processOfficeFile(resource, filename);
                default -> processTextFile(resource, filename);
            };
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to process file", e);
        }
    }

    /**
     * 处理文件并存储到向量存储中
     * @param file 上传的文件
     */
    @Override
    public void processAndStore(MultipartFile file) {
        List<Document> documents = processFile(file);
        if (!documents.isEmpty()) {
            vectorStore.add(documents);
        }
    }

    /**
     * 处理PDF文件
     * @param resource 文件资源
     * @param filename 文件名
     * @return 文档列表
     */
    private List<Document> processPdfFile(ByteArrayResource resource, String filename) {
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder().build();
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.get();
        return documents.stream()
                .map(doc -> {
                    assert doc.getText() != null;
                    return new Document(doc.getText(), addFilenameToMetadata(doc.getMetadata(), filename));
                })
                .toList();
    }

    /**
     * 处理Office文档（Word、Excel、PowerPoint）
     * @param resource 文件资源
     * @param filename 文件名
     * @return 文档列表
     */
    private List<Document> processOfficeFile(ByteArrayResource resource, String filename) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        return documents.stream()
                .map(doc -> {
                    assert doc.getText() != null;
                    return new Document(doc.getText(), addFilenameToMetadata(doc.getMetadata(), filename));
                })
                .toList();
    }

    /**
     * 处理文本文件
     * @param resource 文件资源
     * @param filename 文件名
     * @return 文档列表
     */
    private List<Document> processTextFile(ByteArrayResource resource, String filename) {
        String content = new String(resource.getByteArray());
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filename", filename);
        return List.of(new Document(content, metadata));
    }

    /**
     * 向元数据中添加文件名
     * @param metadata 原元数据
     * @param filename 文件名
     * @return 更新后的元数据
     */
    private Map<String, Object> addFilenameToMetadata(Map<String, Object> metadata, String filename) {
        Map<String, Object> newMetadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        newMetadata.put("filename", filename);
        return newMetadata;
    }

    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 扩展名（不含点）
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}