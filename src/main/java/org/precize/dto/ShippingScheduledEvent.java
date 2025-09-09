package org.precize.dto;

import java.time.Instant;


public class ShippingScheduledEvent extends Event {

    private String orderId;
    private Instant shippingDate;
    @Override
    public String getOrderId() {
        return orderId;
    }
}