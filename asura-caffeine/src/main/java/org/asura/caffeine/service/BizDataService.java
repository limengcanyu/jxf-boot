package org.asura.caffeine.service;

import org.asura.caffeine.dto.GoodsDTO;
import org.asura.caffeine.dto.OrderDTO;
import org.asura.caffeine.dto.UserDTO;

public interface BizDataService {
    // 用户相关
    UserDTO getUserById(Long userId);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long userId);
    void clearAllUserCache();

    // 商品相关
    GoodsDTO getGoodsById(Long goodsId);
    void clearAllGoodsCache();

    // 订单相关
    OrderDTO getOrderById(Long orderId);
    void cancelOrder(Long orderId);

    // 手动操作缓存
    void manualPutCache();
    UserDTO manualGetCache();
    void manualEvictCache();
    void manualClearCache();
    void manualUpdateCache();
    void manualCaffeineCache();
    void onlySpringCache();
}
