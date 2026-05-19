package org.asura.easyexcel.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.asura.easyexcel.dto.ImportDataDTO;
import org.asura.easyexcel.service.SaveDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SaveDataServiceImpl implements SaveDataService {

    /**
     * 批次保存数据到数据库（空实现，仅打印数据）
     * 实际使用时替换为MyBatis Plus的批量插入逻辑
     */
    @Override
    public void saveBatchData(List<ImportDataDTO> dataList, String userId) {
        log.info("用户{}：准备入库数据（批次大小：{}），数据内容：\n{}", userId, dataList.size(), JSONUtil.toJsonPrettyStr(dataList));
        // 此处替换为实际的MyBatis Plus批量插入逻辑
        // example: importDataMapper.insertBatchSomeColumn(dataList);
    }

}
