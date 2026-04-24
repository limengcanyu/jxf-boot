package com.spring.boot.shardingsphere.jdbc.preciseAlgorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * 范围分片算法
 *
 * RangeShardingAlgorithm 是可选的，用于处理 BETWEEN AND, >, <, >=, <=分片，如果不配置 RangeShardingAlgorithm，SQL 中的 BETWEEN AND 将按照全库路由处理。
 *
 */
@Slf4j
public class CommonRangeShardingAlgorithm implements RangeShardingAlgorithm<LocalDateTime> {

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue sharding value
     * @return sharding results for data sources or tables's names
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<LocalDateTime> shardingValue) {
        log.debug("范围分片算法 availableTargetNames: {} shardingValue: {}", availableTargetNames, shardingValue);
        // 范围分片算法 availableTargetNames: [t_order_create_time_2020_q1, t_order_create_time_2020_q2, t_order_create_time_2020_q3, t_order_create_time_2020_q4, t_order_create_time_2021_q1, t_order_create_time_2021_q2, t_order_create_time_2021_q3, t_order_create_time_2021_q4, t_order_create_time_2022_q1, t_order_create_time_2022_q2, t_order_create_time_2022_q3, t_order_create_time_2022_q4, t_order_create_time_2023_q1, t_order_create_time_2023_q2, t_order_create_time_2023_q3, t_order_create_time_2023_q4, t_order_create_time_2024_q1, t_order_create_time_2024_q2, t_order_create_time_2024_q3, t_order_create_time_2024_q4, t_order_create_time_2025_q1, t_order_create_time_2025_q2, t_order_create_time_2025_q3, t_order_create_time_2025_q4]
        // shardingValue: RangeShardingValue(logicTableName=t_order, columnName=create_time, valueRange=(-∞..2020-04-01T00:00])

        String logicTableName = shardingValue.getLogicTableName();

        switch (logicTableName) {
            case "t_order" :
                return TOrderShardingUtils.rangeSharding(availableTargetNames, shardingValue);
            case "t_order_item" :
                return TOrderItemShardingUtils.rangeSharding(availableTargetNames, shardingValue);
            default:
                return Arrays.asList("");
        }
    }
}
