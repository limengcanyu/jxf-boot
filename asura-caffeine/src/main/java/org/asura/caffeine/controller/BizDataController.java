package org.asura.caffeine.controller;

import org.asura.caffeine.service.BizDataService;
import org.asura.caffeine.vo.GoodsVO;
import org.asura.caffeine.vo.OrderVO;
import org.asura.caffeine.vo.UserVO;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserVO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bizDataService.getUserById(userId));
    }

    @PutMapping("/user")
    public ResponseEntity<UserVO> updateUser(@RequestBody UserVO userVO) {
        return ResponseEntity.ok(bizDataService.updateUser(userVO));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        bizDataService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // ========== 商品接口 ==========
    @GetMapping("/goods/{goodsId}")
    public ResponseEntity<GoodsVO> getGoods(@PathVariable Long goodsId) {
        return ResponseEntity.ok(bizDataService.getGoodsById(goodsId));
    }

    @DeleteMapping("/goods/cache")
    public ResponseEntity<Void> clearGoodsCache() {
        bizDataService.clearAllGoodsCache();
        return ResponseEntity.noContent().build();
    }

    // ========== 订单接口 ==========
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderVO> getOrder(@PathVariable Long orderId) {
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
    public ResponseEntity<UserVO> manualGetCache() {
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

