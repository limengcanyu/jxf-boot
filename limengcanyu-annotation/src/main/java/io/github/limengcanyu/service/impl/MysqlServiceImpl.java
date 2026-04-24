package io.github.limengcanyu.service.impl;

import io.github.limengcanyu.annotation.OperationalAudit;
import io.github.limengcanyu.constant.OperationalTypeConst;
import io.github.limengcanyu.service.MysqlService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MysqlServiceImpl implements MysqlService {

    @OperationalAudit(name = "addRecord", type = OperationalTypeConst.OPERATIONAL_TYPE_KEY_ADD)
    @Override
    public String addRecord() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
