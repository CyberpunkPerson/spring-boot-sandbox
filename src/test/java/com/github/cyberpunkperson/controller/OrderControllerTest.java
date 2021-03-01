package com.github.cyberpunkperson.controller;

import com.github.cyberpunkperson.config.cache.dynamo.keygenerator.SimpleDynamoCacheKey;
import com.github.cyberpunkperson.controller.utils.JsonConverter;
import com.github.cyberpunkperson.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;

import static com.github.cyberpunkperson.domain.OrderState.PENDING_APPROVAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = BEFORE_METHOD)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonConverter jsonConverter;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void publishOrderIsOkReturned() throws Exception {

        MvcResult publishResult = mvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.writeToJson(Order.builder()
                        .title("Test order title")
                        .description("Test order description")
                        .state(PENDING_APPROVAL)
                        .price(10d)
                        .createdDate(ZonedDateTime.now())
                        .build())))
                .andExpect(status().isOk())
                .andReturn();

        Order publishedOrder = jsonConverter.readJson(publishResult, Order.class);
        assertNotNull(publishedOrder.getId());
    }

    @Test
    public void findOrderByIdIsOkReturned() throws Exception {

        MvcResult publishResult = mvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.writeToJson(Order.builder()
                        .title("Test order title")
                        .description("Test order description")
                        .state(PENDING_APPROVAL)
                        .price(10d)
                        .build())))
                .andExpect(status().isOk())
                .andReturn();

        Order publishedOrder = jsonConverter.readJson(publishResult, Order.class);
        assertNotNull(publishedOrder.getId());

        MvcResult searchResult = mvc.perform(get("/orders/{orderId}", publishedOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        Order foundOrder = jsonConverter.readJson(searchResult, Order.class);
        assertEquals(publishedOrder, foundOrder);
    }


    @Test
    public void findOrderByHashIsOkReturned() throws Exception {

        Order originalOrder = Order.builder()
                .title("Test order title")
                .description("Test order description")
                .state(PENDING_APPROVAL)
                .price(10d)
                .build();

        MvcResult publishResult = mvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.writeToJson(originalOrder)))
                .andExpect(status().isOk())
                .andReturn();

        Order publishedOrder = jsonConverter.readJson(publishResult, Order.class);
        assertNotNull(publishedOrder.getId());

        MvcResult databaseSearchResult = mvc.perform(get("/orders/hash")
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.writeToJson(publishedOrder)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        Order foundByHashFromDatabase = jsonConverter.readJson(databaseSearchResult, Order.class);
        assertEquals(publishedOrder, foundByHashFromDatabase);

        Order foundByHashFromCache = cacheManager.getCache("order-cache")
                .get(new SimpleDynamoCacheKey(publishedOrder.buildCacheKey(), publishedOrder.getClass()),
                        publishedOrder.getClass());

        assertEquals(publishedOrder, foundByHashFromCache);

    }

    @Test
    public void clearCacheRepositoryIsOkReturned() throws Exception {

        Order originalOrder = Order.builder()
                .title("Test order title")
                .description("Test order description")
                .state(PENDING_APPROVAL)
                .price(10d)
                .build();

        MvcResult publishResult = mvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.writeToJson(originalOrder)))
                .andExpect(status().isOk())
                .andReturn();

        Order publishedOrder = jsonConverter.readJson(publishResult, Order.class);
        assertNotNull(publishedOrder.getId());

        mvc.perform(get("/orders/hash")
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.writeToJson(publishedOrder)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));

        cacheManager.getCache("order-cache").clear();
        assertNull(cacheManager.getCache("order-cache")
                .get(new SimpleDynamoCacheKey(publishedOrder.buildCacheKey(), publishedOrder.getClass()),
                        publishedOrder.getClass()));
    }

}
