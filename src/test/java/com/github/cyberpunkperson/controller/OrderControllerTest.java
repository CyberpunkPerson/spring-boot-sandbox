package com.github.cyberpunkperson.controller;

import com.github.cyberpunkperson.controller.util.JsonConverter;
import com.github.cyberpunkperson.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;

import static com.github.cyberpunkperson.domain.OrderState.PENDING_APPROVAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
                        .createdDate(ZonedDateTime.now())
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

}
