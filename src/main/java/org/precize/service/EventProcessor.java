package org.precize.service;


import org.precize.dto.*;
import org.precize.enums.OrderStatus;
import org.precize.model.Order;
import org.precize.observer.OrderObserver;
import org.precize.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EventProcessor {

    private final OrderRepository orderRepository;
    private final List<OrderObserver> observers = new ArrayList<>();

    public EventProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void processEvent(Event event) {

        if (event instanceof OrderCreatedEvent e) {
            handleOrderCreation(e);
        } else if (event instanceof PaymentReceivedEvent e) {
            handlePayment(e);
        } else if (event instanceof ShippingScheduledEvent e) {
            handleShipping(e);
        } else if (event instanceof OrderCancelledEvent e) {
            handleCancellation(e);
        } else {
            System.out.println("Warning: Unknown event type received: " + event.getClass().getSimpleName());
        }
    }

    private void handleOrderCreation(OrderCreatedEvent event) {
        Order newOrder = new Order(
                event.getOrderId(),
                event.getCustomerId(),
                event.getItems(),
                event.getTotalAmount()
        );
        newOrder.setStatus(OrderStatus.PENDING);
        saveOrderAndUpdateHistory(newOrder, event);
    }

    private void handlePayment(PaymentReceivedEvent event) {
        Order order = findOrderOrLogWarning(event.getOrderId());
        if (order == null) return;

        // Payment logic: check if payment amount matches total amount
        BigDecimal amountPaid = event.getAmountPaid();
        BigDecimal totalAmount = order.getTotalAmount();

        if (amountPaid.compareTo(totalAmount) >= 0) {
            order.setStatus(OrderStatus.PAID);
        } else if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            order.setStatus(OrderStatus.PARTIALLY_PAID);
            System.out.printf("Warning: Partial payment received for Order %s. Received: %s, Required: %s%n",
                    order.getOrderId(), amountPaid, totalAmount);
        } else {
            System.out.printf("Error: Payment amount invalid for Order %s. Amount: %s%n", order.getOrderId(), amountPaid);
        }

        saveOrderAndUpdateHistory(order, event);
    }

    private void handleShipping(ShippingScheduledEvent event) {
        Order order = findOrderOrLogWarning(event.getOrderId());
        if (order == null) return;

        // Shipping logic: usually only possible if order is paid
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.PARTIALLY_PAID) {
            order.setStatus(OrderStatus.SHIPPED);
            saveOrderAndUpdateHistory(order, event);
        } else {
            System.out.printf("Warning: Cannot ship order %s. Status is %s (not PAID).%n", order.getOrderId(), order.getStatus());
        }
    }

    private void handleCancellation(OrderCancelledEvent event) {
        Order order = findOrderOrLogWarning(event.getOrderId());
        if (order == null) return;

        order.setStatus(OrderStatus.CANCELLED);
        saveOrderAndUpdateHistory(order, event);
    }

    // --- Helper Methods ---

    private Order findOrderOrLogWarning(String orderId) {
        return orderRepository.findById(orderId)
                .orElseGet(() -> {
                    System.out.println("Warning: Received event for non-existent order ID: " + orderId);
                    return null;
                });
    }

    private void saveOrderAndUpdateHistory(Order order, Event event) {
        order.addEventToHistory(event);
        orderRepository.save(order);
        notifyObservers(order, event);
    }

    private void notifyObservers(Order order, Event event) {
        for (OrderObserver observer : observers) {
            observer.update(order, event);
        }
    }
}