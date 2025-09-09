package org.precize.enums;

public enum OrderStatus {
    PENDING(0), PAID(1), PARTIALLY_PAID(2), SHIPPED (3), CANCELLED(4), FAILED(5) ;

    int orderStatus;
    OrderStatus(int orderStatus){
        this.orderStatus = orderStatus;
    }
}