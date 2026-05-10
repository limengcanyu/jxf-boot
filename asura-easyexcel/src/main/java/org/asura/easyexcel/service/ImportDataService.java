package org.asura.easyexcel.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 导入核心业务服务
 */
public interface ImportDataService {

    void importFile(MultipartFile file, String userId);

}
