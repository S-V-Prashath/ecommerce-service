package org.precize.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.precize.enums.OrderStatus;
import org.precize.dto.Event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Order {

    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<Event> eventHistory;

    public Order(String orderId, String customerId, List<OrderItem> items, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING; // Initial status
        this.eventHistory = new ArrayList<>();
    }

    public void addEventToHistory(Event event) {
        this.eventHistory.add(event);
    }


}