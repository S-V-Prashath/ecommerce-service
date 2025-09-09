package org.precize.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "OrderCreated"),
        @JsonSubTypes.Type(value = PaymentReceivedEvent.class, name = "PaymentReceived"),
        @JsonSubTypes.Type(value = ShippingScheduledEvent.class, name = "ShippingScheduled"),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "OrderCancelled")
})
@JsonIgnoreProperties(ignoreUnknown = true) // Gracefully handle extra fields in JSON
@Getter
@Setter
public abstract class Event {

    private String eventId;
    private Instant timestamp;

    public abstract String getOrderId();


}