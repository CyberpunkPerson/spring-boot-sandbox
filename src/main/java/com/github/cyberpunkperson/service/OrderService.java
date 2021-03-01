package com.github.cyberpunkperson.service;

import com.github.cyberpunkperson.domain.Order;
import com.github.cyberpunkperson.repository.OrderResository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderResository orderResository;


    public Order save(Order order) {
        return orderResository.save(order);
    }

    public List<Order> findAll() {
        return orderResository.findAll();
    }

    public Order findById(UUID orderId) {
        return orderResository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException(orderId, Order.class.getName()));
    }

    public void deleteById(UUID orderId) {
        orderResository.deleteById(orderId);
    }

    @Cacheable(value = "order-cache", keyGenerator = "dynamoCacheKeyGenerator")
    public Order findByHash(Order searchOrder) {

        String hash = searchOrder.buildCacheKey();
        return orderResository.findAll().stream()
                .filter(order -> order.buildCacheKey().equals(hash))
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException(hash, Order.class.getName()));
    }

}
