package com.amdocs.poc.axon.saga.order.controller;

import com.amdocs.poc.axon.saga.order.command.CreateOrderCommand;
import com.amdocs.poc.axon.saga.order.event.OrderEventPlayer;
import com.amdocs.poc.axon.saga.order.model.MultipleOrderModel;
import com.amdocs.poc.axon.saga.order.model.OrderModel;
import com.amdocs.poc.axon.saga.core.model.OrderStatus;
import com.amdocs.poc.axon.saga.order.model.ReplayOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@RestController
@RequestMapping("/axon-saga")
@Slf4j
public class OrderController {
    private CommandGateway commandGateway;
    private OrderEventPlayer orderEventPlayer;


    @Autowired
    public OrderController(CommandGateway commandGateway, OrderEventPlayer orderEventPlayer) {
        this.commandGateway = commandGateway;
        this.orderEventPlayer = orderEventPlayer;

        log.info("OrderController constructor for commandGateway: {}, orderEventPlayer: {}",
                commandGateway, orderEventPlayer);
    }

    @PostMapping(value = "/create-order")
    public String createOrder(@RequestBody OrderModel order) {
        String orderId = UUID.randomUUID().toString();
        log.debug("OrderController.createOrder for orderId: {}", orderId);

        try {
            CreateOrderCommand createOrderCommand =
                    CreateOrderCommand.builder()
                            .orderId(orderId)
                            .userId(order.getUserId())
                            .addressId(order.getAddressId())
                            .productId(order.getProductId())
                            .quantity(order.getQuantity())
                            .orderStatus(OrderStatus.CREATED)
                            .build();
            commandGateway.sendAndWait(createOrderCommand);
            log.debug("OrderController.createOrder Order with orderId {} created", orderId);
        } catch (Exception ex) {
            log.error("OrderController.createOrder got Exception: {}", ex.getMessage());
        }

        log.info("OrderController.createOrder for orderId: {} - End", orderId);
        return "Order Created";
    }

    @PostMapping(value = "/create-orders")
    public String createOrders(@RequestBody MultipleOrderModel orders) {
        log.info("OrderController.createOrders for orderCount: {} - Start", orders.getOrderCount());
        List<String> productList = Arrays.asList("Orange", "Apple", "Cherry", "Tomato", "Potato");
        long idx = 0;
        while(idx++ < orders.getOrderCount())
        {
            String orderId = UUID.randomUUID().toString();
            log.debug("OrderController.createOrders generated orderId: {}", orderId);
            try {
                CreateOrderCommand createOrderCommand =
                        CreateOrderCommand.builder()
                                .orderId(orderId)
                                .userId("andreyz")
                                .addressId("Mytishchi")
                                .productId(productList.get(new Random().nextInt(productList.size())))
                                .quantity(new Random().nextInt(26) + 1)
                                .orderStatus(OrderStatus.CREATED)
                                .build();
                commandGateway.sendAndWait(createOrderCommand);
                log.debug("OrderController.createOrders Order with orderId: {} created", orderId);
            } catch (Exception ex) {
                log.error("OrderController.createOrders with orderId: {} got Exception: {}",
                        orderId, ex.getMessage());
            }
        }

        log.info("OrderController.createOrders for orderCount: {} - End", orders.getOrderCount());
        return "Orders Created";
    }

    @PostMapping(value = "/replay-orders")
    public String replayOrders(@RequestBody ReplayOrderModel orders) {
        String processingGroup = orders.getProcessingGroup();
        Long startPos = orders.getStartPos();
        log.info("OrderController.replayOrders for processingGroup: {}, startPos: {} - Start",
                processingGroup, startPos);
        orderEventPlayer.replay(processingGroup, startPos);

        log.info("OrderController.replayOrders for processingGroup: {}, startPos: {} - End",
                processingGroup, startPos);
        return "All Orders Replayed";
    }

    @PostMapping(value = "/replay-progress")
    public String getReplayProgress(@RequestBody ReplayOrderModel orders) {
        String processingGroup = orders.getProcessingGroup();
        log.info("OrderController.getReplayProgress for processingGroup: {} - Start", processingGroup);

        String progress;
        if(orderEventPlayer.getProgress(processingGroup).isPresent()) {
            progress = "Progress: " +
            NumberFormat.getNumberInstance().format(orderEventPlayer.getProgress(processingGroup).get().getProgress());
        } else {
            progress = "Not able to calculate Progress";
        }

        log.info("OrderController.getReplayProgress for processingGroup: {} - End", processingGroup);
        return progress;
    }

}
