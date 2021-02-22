package com.github.cyberpunkperson.controller;

import com.github.cyberpunkperson.domain.Order;
import com.github.cyberpunkperson.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public Order save(Order order) {
        return orderService.save(order);
    }

    @PutMapping
    public Order update(Order order) {
        return orderService.save(order);
    }

    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{orderId}")
    public Order findById(@PathVariable UUID orderId) {
        return orderService.findById(orderId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteById(@PathVariable UUID orderId) {
        orderService.deleteById(orderId);
    }

}
