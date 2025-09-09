package org.precize.observer;


import org.precize.Constants.MessageConstants;
import org.precize.dto.Event;
import org.precize.model.Order;



public class AlertObserver implements OrderObserver {

    @Override
    public void update(Order order, Event event) {
        if (MessageConstants.ALERT_STATUSES.contains(order.getStatus())) {
            System.out.printf(MessageConstants.ALERT_OBSERVER_FORMAT,
                    order.getOrderId(),
                    order.getStatus());
        }
    }
}