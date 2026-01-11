package com.spring.boot.caffeine.service;

import com.spring.boot.caffeine.vo.GoodsVO;
import com.spring.boot.caffeine.vo.OrderVO;
import com.spring.boot.caffeine.vo.UserVO;

public interface BizDataService {
    // 用户相关
    UserVO getUserById(Long userId);
    UserVO updateUser(UserVO userVO);
    void deleteUser(Long userId);

    // 商品相关
    GoodsVO getGoodsById(Long goodsId);
    void clearAllGoodsCache();

    // 订单相关
    OrderVO getOrderById(Long orderId);
    void cancelOrder(Long orderId);

    // 手动操作缓存
    void manualPutCache();
    UserVO manualGetCache();
    void manualEvictCache();
    void manualClearCache();
    void manualUpdateCache();
    void manualCaffeineCache();
    void onlySpringCache();
}

