package org.precize.observer;


import org.precize.dto.Event;
import org.precize.model.Order;


public interface OrderObserver {
    void update(Order order, Event event);
}