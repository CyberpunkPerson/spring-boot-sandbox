package com.github.cyberpunkperson.domain;

import com.github.cyberpunkperson.config.cache.CacheKey;
import com.github.cyberpunkperson.config.cache.CacheKeyOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order implements CacheKey {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @CacheKeyOperation.Exclude
    private String title;

    private String description;

    private OrderState state;

    private Double price;

    @CacheKeyOperation.Exclude
    private ZonedDateTime createdDate;

}
