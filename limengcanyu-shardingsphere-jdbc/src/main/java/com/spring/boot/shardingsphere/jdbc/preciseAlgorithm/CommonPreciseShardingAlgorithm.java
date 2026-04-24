package com.spring.boot.shardingsphere.jdbc.preciseAlgorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 精确分片算法
 *
 * PreciseShardingAlgorithm 是必选的，用于处理 = 和 IN 的分片。
 *
 */
@Slf4j
public class CommonPreciseShardingAlgorithm implements PreciseShardingAlgorithm<LocalDateTime> {

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
        log.debug("精确分片算法 availableTargetNames: {} shardingValue: {}", availableTargetNames, shardingValue);
        // 精确分片算法 availableTargetNames: [t_order_create_time_2020_q1, t_order_create_time_2020_q2, t_order_create_time_2020_q3, t_order_create_time_2020_q4, t_order_create_time_2021_q1, t_order_create_time_2021_q2, t_order_create_time_2021_q3, t_order_create_time_2021_q4, t_order_create_time_2022_q1, t_order_create_time_2022_q2, t_order_create_time_2022_q3, t_order_create_time_2022_q4, t_order_create_time_2023_q1, t_order_create_time_2023_q2, t_order_create_time_2023_q3, t_order_create_time_2023_q4, t_order_create_time_2024_q1, t_order_create_time_2024_q2, t_order_create_time_2024_q3, t_order_create_time_2024_q4, t_order_create_time_2025_q1, t_order_create_time_2025_q2, t_order_create_time_2025_q3, t_order_create_time_2025_q4]
        // shardingValue: PreciseShardingValue(logicTableName=t_order, columnName=create_time, value=2020-09-02T15:59:24.716732200)

        String logicTableName = shardingValue.getLogicTableName();

        switch (logicTableName) {
            case "t_order" :
                return TOrderShardingUtils.preciseSharding(availableTargetNames, shardingValue);
            case "t_order_item" :
                return TOrderItemShardingUtils.preciseSharding(availableTargetNames, shardingValue);
            default:
                return "";
        }
    }
}
