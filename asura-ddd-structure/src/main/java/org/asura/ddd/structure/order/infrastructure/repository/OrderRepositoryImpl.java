package org.asura.ddd.structure.order.infrastructure.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.asura.ddd.structure.order.domain.model.aggregate.Order;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;
import org.asura.ddd.structure.order.domain.model.valueobject.OrderStatus;
import org.asura.ddd.structure.order.domain.model.valueobject.ShippingAddress;
import org.asura.ddd.structure.order.domain.repository.OrderRepository;
import org.asura.ddd.structure.order.infrastructure.persistence.entity.OrderEntity;
import org.asura.ddd.structure.order.infrastructure.persistence.entity.OrderItemEntity;
import org.asura.ddd.structure.order.infrastructure.persistence.mapper.OrderItemMapper;
import org.asura.ddd.structure.order.infrastructure.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderRepositoryImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = toOrderEntity(order);
        
        if (orderMapper.selectById(order.getId()) != null) {
            orderMapper.updateById(orderEntity);
        } else {
            orderMapper.insert(orderEntity);
        }
        
        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = toOrderItemEntity(item);
            itemEntity.setOrderId(order.getId());
            if (orderItemMapper.selectById(itemEntity.getId()) != null) {
                orderItemMapper.updateById(itemEntity);
            } else {
                orderItemMapper.insert(itemEntity);
            }
        }
        
        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        OrderEntity entity = orderMapper.selectById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toOrder(entity));
    }

    @Override
    public List<Order> findByUserId(String userId) {
        List<OrderEntity> entities = orderMapper.selectByUserId(userId);
        return entities.stream().map(this::toOrder).collect(Collectors.toList());
    }

    @Override
    public List<Order> findByUserIdAndStatus(String userId, String status) {
        List<OrderEntity> entities = orderMapper.selectByUserIdAndStatus(userId, status);
        return entities.stream().map(this::toOrder).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        orderItemMapper.deleteByOrderId(id);
        orderMapper.deleteById(id);
    }

    public IPage<Order> findPage(int pageNum, int pageSize, String userId, String status) {
        Page<OrderEntity> page = new Page<>(pageNum, pageSize);
        IPage<OrderEntity> entityPage = orderMapper.selectPageByCondition(page, userId, status);
        return entityPage.convert(this::toOrder);
    }

    public List<Order> findList(String userId, String status) {
        return orderMapper.selectPageByCondition(new Page<>(), userId, status)
                .getRecords()
                .stream()
                .map(this::toOrder)
                .collect(Collectors.toList());
    }

    private OrderEntity toOrderEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        
        if (order.getShippingAddress() != null) {
            ShippingAddress address = order.getShippingAddress();
            entity.setShippingAddressProvince(address.getProvince());
            entity.setShippingAddressCity(address.getCity());
            entity.setShippingAddressDistrict(address.getDistrict());
            entity.setShippingAddressDetail(address.getDetail());
            entity.setShippingAddressZipCode(address.getZipCode());
        }
        
        entity.setTotalAmount(order.getTotalAmount());
        entity.setStatus(order.getStatus().name());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        return entity;
    }

    private OrderItemEntity toOrderItemEntity(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setQuantity(item.getQuantity());
        return entity;
    }

    private Order toOrder(OrderEntity entity) {
        ShippingAddress shippingAddress = entity.getShippingAddressProvince() != null ? ShippingAddress.create(
                entity.getShippingAddressProvince(),
                entity.getShippingAddressCity(),
                entity.getShippingAddressDistrict(),
                entity.getShippingAddressDetail(),
                entity.getShippingAddressZipCode()
        ) : null;

        Order order = Order.reconstruct(
                entity.getId(),
                entity.getUserId(),
                shippingAddress,
                entity.getTotalAmount(),
                OrderStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );

        List<OrderItemEntity> itemEntities = orderItemMapper.selectByOrderId(entity.getId());
        for (OrderItemEntity itemEntity : itemEntities) {
            OrderItem item = OrderItem.create(
                    itemEntity.getProductId(),
                    itemEntity.getProductName(),
                    itemEntity.getUnitPrice(),
                    itemEntity.getQuantity()
            );
            item.setId(itemEntity.getId());
            order.addItemForReconstruct(item);
        }

        return order;
    }
}