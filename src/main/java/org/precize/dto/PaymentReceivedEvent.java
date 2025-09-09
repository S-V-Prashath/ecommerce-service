package org.precize.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentReceivedEvent extends Event {
    private String orderId;
    private BigDecimal amountPaid;
}