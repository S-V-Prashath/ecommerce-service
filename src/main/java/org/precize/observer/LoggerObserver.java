package org.precize.observer;


import org.precize.Constants.MessageConstants;
import org.precize.dto.Event;
import org.precize.model.Order;


public class LoggerObserver implements OrderObserver {

    @Override
    public void update(Order order, Event event) {
        System.out.printf(MessageConstants.LOGGER_OBSERVER_FORMAT,
                event.getClass().getSimpleName(),
                event.getEventId(),
                order.getOrderId(),
                order.getStatus());
    }
}