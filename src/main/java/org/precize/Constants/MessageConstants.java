package org.precize.Constants;

import org.precize.enums.OrderStatus;

import java.util.Set;

public class MessageConstants {
    public static final  String LOGGER_OBSERVER_FORMAT = "[Logger] Event processed: %s (ID: %s) | OrderID: %s | New Status: %s%n";
    public static final  String ALERT_OBSERVER_FORMAT ="🚨 [Alert System] Sending alert for Order %s: Status changed to %s%n";
    public static final Set<OrderStatus> ALERT_STATUSES = Set.of(OrderStatus.PAID, OrderStatus.SHIPPED, OrderStatus.CANCELLED, OrderStatus.FAILED);

}
