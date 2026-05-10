package org.asura.caffeine.controller;

import lombok.RequiredArgsConstructor;
import org.asura.caffeine.dto.GoodsDTO;
import org.asura.caffeine.dto.OrderDTO;
import org.asura.caffeine.dto.UserDTO;
import org.asura.caffeine.service.BizDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 生产级RESTful接口
 * 所有接口均可直接测试缓存的新增/更新/删除/过期等行为，控制台会打印监听日志
 */
@RestController
@RequestMapping("/biz")
@RequiredArgsConstructor
public class BizDataController {

    private final BizDataService bizDataService;

    // ========== 用户接口 ==========

    /**
     * http://localhost:8080/prod/biz/getUser/1001
     */
    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bizDataService.getUserById(userId));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userVO) {
        return ResponseEntity.ok(bizDataService.updateUser(userVO));
    }

    /**
     * http://localhost:8080/prod/biz/deleteUser/1001
     */
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        bizDataService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * http://localhost:8080/user/clearAllUserCache
     * @return
     */
    @GetMapping("/clearAllUserCache")
    public void clearAllUserCache() {
        bizDataService.clearAllUserCache();
    }

    // ========== 商品接口 ==========
    @GetMapping("/goods/{goodsId}")
    public ResponseEntity<GoodsDTO> getGoods(@PathVariable Long goodsId) {
        return ResponseEntity.ok(bizDataService.getGoodsById(goodsId));
    }

    @DeleteMapping("/goods/cache")
    public ResponseEntity<Void> clearGoodsCache() {
        bizDataService.clearAllGoodsCache();
        return ResponseEntity.noContent().build();
    }

    // ========== 订单接口 ==========
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(bizDataService.getOrderById(orderId));
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        bizDataService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    // ========== 手动操作 ==========
    @PutMapping("/manualPutCache")
    public ResponseEntity<Void> manualPutCache() {
        bizDataService.manualPutCache();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/manualGetCache")
    public ResponseEntity<UserDTO> manualGetCache() {
        return ResponseEntity.ok(bizDataService.manualGetCache());
    }

    @PutMapping("/manualEvictCache")
    public ResponseEntity<Void> manualEvictCache() {
        bizDataService.manualEvictCache();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/manualClearCache")
    public ResponseEntity<Void> manualClearCache() {
        bizDataService.manualClearCache();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/manualUpdateCache")
    public ResponseEntity<Void> manualUpdateCache() {
        bizDataService.manualUpdateCache();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/manualCaffeineCache")
    public ResponseEntity<Void> manualCaffeineCache() {
        bizDataService.manualCaffeineCache();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/onlySpringCache")
    public ResponseEntity<Void> onlySpringCache() {
        bizDataService.onlySpringCache();
        return ResponseEntity.noContent().build();
    }

}
