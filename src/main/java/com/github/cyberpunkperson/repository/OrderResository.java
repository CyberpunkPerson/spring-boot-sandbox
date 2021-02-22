package com.github.cyberpunkperson.repository;

import com.github.cyberpunkperson.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderResository extends JpaRepository<Order, UUID> {
}
