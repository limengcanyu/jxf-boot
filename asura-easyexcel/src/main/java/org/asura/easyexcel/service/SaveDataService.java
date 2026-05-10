package org.asura.easyexcel.service;

import org.asura.easyexcel.dto.ImportDataDTO;

import java.util.List;

/**
 * 保存核心业务服务
 */
public interface SaveDataService {

    void saveBatchData(List<ImportDataDTO> dataList, String userId);

}
