package com.github.cyberpunkperson.service;

import com.github.cyberpunkperson.config.cache.dynamo.keygenerator.SimpleDynamoCacheKey;
import com.github.cyberpunkperson.domain.Order;
import com.github.cyberpunkperson.repository.OrderResository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.cyberpunkperson.domain.OrderState.PENDING_APPROVAL;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderResository orderRepository;

    @MockBean
    private CacheManager cacheManager;

    @Mock
    private Cache cache;


    @Test
    public void findOrderByHashFromCacheNoMethodInvocation() {

        Order originalOrder = Order.builder()
                .title("Test order title")
                .description("Test order description")
                .state(PENDING_APPROVAL)
                .price(10d)
                .build();

        when(orderRepository.findAll())
                .thenReturn(singletonList(originalOrder));

        when(cacheManager.getCache("order-cache"))
                .thenReturn(cache);

        when(cache.get(new SimpleDynamoCacheKey(originalOrder.buildCacheKey(), Order.class)))
                .thenReturn(null);


        orderService.findByHash(originalOrder);
        verify(orderRepository, times(1)).findAll();

        when(cache.get(new SimpleDynamoCacheKey(originalOrder.buildCacheKey(), Order.class)))
                .thenReturn(new SimpleValueWrapper(originalOrder));

        orderService.findByHash(originalOrder);
        verify(orderRepository, times(1)).findAll();
    }
}
