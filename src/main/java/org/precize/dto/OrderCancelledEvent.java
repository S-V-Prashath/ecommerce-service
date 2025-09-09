package org.precize.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class OrderCancelledEvent extends Event {

    private String orderId;
    private String reason;

}