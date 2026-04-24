package com.spring.boot.shardingsphere.jdbc.preciseAlgorithm;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
public class TOrderItemShardingUtils {
    /**
     * logicTableName_{year}_q{quarter}
     * 按季度精确分片
     *
     * @param availableTargetNames 可用的真实表集合
     * @param shardingValue 分片值
     * @return
     */
    public static String preciseSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
        // 根据等值查询条件，计算出匹配的真实表

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        int year = shardingValue.getValue().getYear();
        int month = shardingValue.getValue().getMonthValue();
        log.debug("新增记录所属月份: {}", year + "-" + month);

        String tablePattern = "t_order_item_create_time_YEAR_qDU".replace("YEAR", year + "");

        if (month <= 3) {
            return tablePattern.replace("DU", "1");
        }

        if (month <= 6) {
            return tablePattern.replace("DU", "2");
        }

        if (month <= 9) {
            return tablePattern.replace("DU", "3");
        }

        return tablePattern.replace("DU", "4");
    }

    /**
     * logicTableName_{year}_q{quarter}
     * 按季度范围分片
     *
     * @param availableTargetNames 可用的真实表集合
     * @param shardingValue 分片值
     * @return
     */
    public static Collection<String> rangeSharding(Collection<String> availableTargetNames, RangeShardingValue<LocalDateTime> shardingValue) {
        // 根据范围查询条件，筛选出匹配的真实表集合

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        Range<LocalDateTime> range = shardingValue.getValueRange();

//        LocalDateTime lower = range.lowerEndpoint(); // 范围下限：可能为null
//        LocalDateTime upper = range.upperEndpoint(); // 范围上限：2020-04-01T00:00

//        return Arrays.asList("t_order_create_time_2020_q1");
//        return Arrays.asList("t_order_create_time_2020_q1", "t_order_create_time_2020_q2");
        return availableTargetNames;
    }
}
