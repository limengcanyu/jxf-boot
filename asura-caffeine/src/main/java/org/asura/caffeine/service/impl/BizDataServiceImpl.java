package org.asura.caffeine.service.impl;

import org.asura.caffeine.config.CaffeineCacheConfig;
import org.asura.caffeine.service.BizDataService;
import org.asura.caffeine.vo.GoodsVO;
import org.asura.caffeine.vo.OrderVO;
import org.asura.caffeine.vo.UserVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 生产级业务实现类
 * 核心：使用SpringCache原生注解操作缓存，业务代码无任何缓存逻辑侵入
 * 注解说明：@Cacheable(查)、@CachePut(更)、@CacheEvict(删)
 */
@Slf4j
@Service
public class BizDataServiceImpl implements BizDataService {

    // 手动操作缓存 方式一：注入 Spring CacheManager 手动操作（✅ 推荐、生产最优、最优雅）
    @Resource
    private CacheManager cacheManager;

    // 手动操作缓存 方式二：注入 Caffeine 原生 Cache 手动操作（✅ 底层方式，适合特殊场景）
    // 注入Caffeine原生缓存
    @Resource(name = CaffeineCacheConfig.CACHE_USER)
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> userCache;

    // ====================== 用户缓存操作 (userCache:写入5分钟过期) ======================
    @Override
    @Cacheable(value = CaffeineCacheConfig.CACHE_USER, key = "#userId", unless = "#result == null")
    public UserVO getUserById(Long userId) {
        log.info("【用户缓存未命中】执行数据库查询 -> userId:{}", userId);
        // 生产环境替换为：Mybatis/JPA 查询数据库
        return new UserVO(userId, "用户_" + userId, 25, "13800138000");
    }

    @Override
    @CachePut(value = CaffeineCacheConfig.CACHE_USER, key = "#userVO.userId")
    public UserVO updateUser(UserVO userVO) {
        log.info("【更新用户】执行数据库更新 -> userId:{}", userVO.getUserId());
        // 生产环境替换为：Mybatis/JPA 更新数据库
        return userVO;
    }

    @Override
    @CacheEvict(value = CaffeineCacheConfig.CACHE_USER, key = "#userId")
    public void deleteUser(Long userId) {
        log.info("【删除用户】执行数据库删除 -> userId:{}", userId);
        // 生产环境替换为：Mybatis/JPA 删除数据库
    }

    // ====================== 商品缓存操作 (goodsCache:访问1小时过期) ======================
    @Override
    @Cacheable(value = CaffeineCacheConfig.CACHE_GOODS, key = "#goodsId", unless = "#result == null")
    public GoodsVO getGoodsById(Long goodsId) {
        log.info("【商品缓存未命中】执行数据库查询 -> goodsId:{}", goodsId);
        return new GoodsVO(goodsId, "商品_" + goodsId, new BigDecimal("999.00"), 1000);
    }

    @Override
    @CacheEvict(value = CaffeineCacheConfig.CACHE_GOODS, allEntries = true)
    public void clearAllGoodsCache() {
        log.info("【商品缓存】执行批量清空操作");
    }

    // ====================== 订单缓存操作 (orderCache:写入2分钟过期+异步刷新) ======================
    @Override
    @Cacheable(value = CaffeineCacheConfig.CACHE_ORDER, key = "#orderId", unless = "#result == null")
    public OrderVO getOrderById(Long orderId) {
        log.info("【订单缓存未命中】执行数据库查询 -> orderId:{}", orderId);
        return new OrderVO(orderId, 1001L, 2001L, new BigDecimal("1999.00"), LocalDateTime.now());
    }

    @Override
    @CacheEvict(value = CaffeineCacheConfig.CACHE_ORDER, key = "#orderId")
    public void cancelOrder(Long orderId) {
        log.info("【取消订单】执行数据库更新 + 删除缓存 -> orderId:{}", orderId);
    }

    // ========== 新增：手动操作缓存的示例方法（生产常用的5种操作，全部监听生效） ==========
    /**
     * 手动新增/写入缓存
     * 触发：【缓存新增】监听日志
     */
    @Override
    public void manualPutCache() {
        // 1. 获取指定的缓存空间（比如userCache）
        Cache userCache = cacheManager.getCache(CaffeineCacheConfig.CACHE_USER);
        // 2. 手动写入缓存 key=1002, value=自定义用户对象
        UserVO userVO = new UserVO(1002L, "手动新增用户", 30, "13900139000");
        assert userCache != null;
        userCache.put(1002L, userVO);
        log.info("✅ 手动写入缓存成功，key=1002");
    }

    /**
     * 手动查询缓存
     * 无监听触发（查询不会修改缓存，监听只针对「新增/移除」行为）
     */
    @Override
    public UserVO manualGetCache() {
        Cache userCache = cacheManager.getCache(CaffeineCacheConfig.CACHE_USER);
        // 手动查询缓存，返回值是Cache.ValueWrapper，需要手动解包
        assert userCache != null;
        Cache.ValueWrapper wrapper = userCache.get(1002L);
        if (wrapper != null) {
            return (UserVO) wrapper.get();
        }
        log.info("❌ 手动查询缓存未命中，key=1002");
        return null;
    }

    /**
     * 手动删除单个缓存
     * 触发：【主动删除/批量清空】监听日志（RemovalCause.EXPLICIT）
     */
    @Override
    public void manualEvictCache() {
        Cache userCache = cacheManager.getCache(CaffeineCacheConfig.CACHE_USER);
        assert userCache != null;
        userCache.evict(1002L);
        log.info("✅ 手动删除缓存成功，key=1002");
    }

    /**
     * 手动批量清空整个缓存空间的所有缓存
     * 触发：缓存空间内每一条缓存都会打印【主动删除/批量清空】监听日志（RemovalCause.EXPLICIT）
     */
    @Override
    public void manualClearCache() {
        Cache goodsCache = cacheManager.getCache(CaffeineCacheConfig.CACHE_GOODS);
        assert goodsCache != null;
        goodsCache.clear();
        log.info("✅ 手动批量清空goodsCache所有缓存成功");
    }

    /**
     * 手动更新缓存（同一个key重新put新值）
     * 触发：先打印【缓存更新(REPLACED)】监听日志（旧值被移除），再打印【缓存新增】监听日志（新值写入）
     */
    @Override
    public void manualUpdateCache() {
        Cache userCache = cacheManager.getCache(CaffeineCacheConfig.CACHE_USER);
        UserVO newUserVO = new UserVO(1002L, "手动更新后的用户", 31, "13900139000");
        assert userCache != null;
        userCache.put(1002L, newUserVO);
        log.info("✅ 手动更新缓存成功，key=1002");
    }

    // 手动操作
    @Override
    public void manualCaffeineCache() {
        // 新增/更新 原生操作（无监听）
        userCache.put(1003L, new UserVO(1003L, "原生缓存用户", 28, "13700137000"));
        // 查询 原生查询（无监听）
        UserVO userVO = (UserVO) userCache.getIfPresent(1003L);
        // 删除 原生删除 → 触发【主动删除】监听日志 ✔️
        userCache.invalidate(1003L);
        // 批量清空 原生删除 → 触发【主动删除】监听日志 ✔️
        userCache.invalidateAll();
    }

    /**
     * 统一用「Spring 封装的 Cache」做所有手动操作
     */
    @Override
    public void onlySpringCache() {
        org.springframework.cache.Cache userCache = cacheManager.getCache(CaffeineCacheConfig.CACHE_USER);
        Long userId = 1003L;
        UserVO userVO = new UserVO(userId, "原生缓存用户", 28, "13700137000");

        // 新增 → 【缓存新增】日志 ✔️
        assert userCache != null;
        userCache.put(userId, userVO);
        // 查询 → 无日志
        UserVO cacheUser = (UserVO) Objects.requireNonNull(userCache.get(userId)).get();
        // 删除 → 【主动删除】日志 ✔️
        userCache.evict(userId);
        // 批量清空 → 批量【主动删除】日志 ✔️
        userCache.clear();
    }

}

