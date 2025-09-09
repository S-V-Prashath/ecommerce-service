package org.precize.repository;


import org.precize.model.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class OrderRepository {

    private final Map<String, Map<String, Order>> customerStore = new ConcurrentHashMap<>();
    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();

    public void save(Order order) {
        orderStore.put(order.getOrderId(), order);
        customerStore
                .computeIfAbsent(order.getCustomerId(), k -> new ConcurrentHashMap<>())
                .put(order.getOrderId(), order);
    }

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orderStore.get(orderId));
    }

    public Map<String, Map<String, Order>> getAll(){
        return customerStore;
    }
}