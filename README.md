Event-Driven Order Processing System Documentation

1. Introduction
   This document describes the design and implementation of a simplified event-driven order processing system in Java. The system simulates the backend of an e-commerce platform and handles order-related events, updating order states and notifying observers when significant changes occur.

2. Objective
* Build a backend system that processes various order-related events such as:
  * Order creation
  * Payment receipt
  * Shipping scheduling
  * Order cancellation
* Update order states in response to events.
* Notify observers (LoggerObserver and AlertObserver) for significant events.

3. Model

   * Order
     * Attributes:
       * orderId (String)
       * customerId (String)
       * items (List<OrderItem>)
       * totalAmount (BigDecimal)
       * status (OrderStatus enum: PENDING, PAID, PARTIALLY\_PAID, SHIPPED, CANCELLED, FAILED)
       * eventHistory (List<Event>)
   * OrderItem
     * Attributes: itemId (String), quantity (int)

4. Event Model
   * Abstract class Event:
     * eventId (String)
     * timestamp (Instant)
     * getOrderId() (abstract method)
   * Concrete event types:
     * OrderCreatedEvent: includes orderId, customerId, items, totalAmount
     * PaymentReceivedEvent: includes orderId, amountPaid
     * ShippingScheduledEvent: includes orderId, shippingDate
     * OrderCancelledEvent: includes orderId, reason

5. Repositories
   * OrderRepository:
     * Stores orders in memory using:
       * orderStore (Map\<String, Order>) – lookup by orderId
       * customerStore (Map\<String, Map\<String, Order>>) – orders grouped by customerId
     * Methods:
       * save(Order order)
       * findById(String orderId)
       * getAll() – returns all orders grouped by customer
6. Event Ingestion
   * EventReader:
     * Reads events from a text file with one JSON object per line.
     * Parses each line into the corresponding Event subclass using Jackson (ObjectMapper with JavaTimeModule).
     * Filters out invalid or empty lines.

7. Event Processing
    * EventProcessor:
     * Processes events and updates orders.
     * Handles each event type with specific logic:
       * OrderCreatedEvent: Creates a new order with PENDING status.
       * PaymentReceivedEvent: Updates order status to PAID or PARTIALLY\_PAID.
       * ShippingScheduledEvent: Updates status to SHIPPED (only if order is paid).
       * OrderCancelledEvent: Updates status to CANCELLED.
     * Unknown events are logged as warnings.
     * Notifies observers after updating order state.

8. Observer Mechanism
   * Interface: OrderObserver
     * Method: update(Order order, Event event)
   * Observers:
     1. LoggerObserver
        * Logs event type, event ID, order ID, and new status.
     2. AlertObserver
        * Sends alerts for critical status changes: PAID, SHIPPED, CANCELLED, FAILED

9. Event Routing
    * Event routing is handled using instanceof pattern matching in EventProcessor.processEvent(Event event).
    * Based on the runtime type of the event, the appropriate handler method is called.

10. JSON Event Ingestion
  * Sample event JSON:
    {
    "eventId": "e1",
    "timestamp": "2025-07-29T10:00:00Z",
    "eventType": "OrderCreated",
    "orderId": "ORD001",
    "customerId": "CUST001",
    "items": \[{"itemId": "P001", "qty": 2}],
    "totalAmount": 100.00
    }

11. Storage Logic
    * Orders are stored in-memory:
      * orderStore: For fast lookup by orderId.
      * customerStore: Nested map for grouping orders by customerId.
    * OrderRepository.save(Order order) uses computeIfAbsent to create a customer map if it doesn’t exist.

12. Utilities
  * CommonUtil.prettyPrintHistory():
    * Uses Jackson ObjectMapper to convert orders into a pretty JSON string.
    * Enables SerializationFeature.INDENT\_OUTPUT and registers JavaTimeModule for Instant support.

13. Error Handling
* Invalid JSON lines in the event file are logged with a warning.
* Events referencing non-existent orders are logged but do not stop processing.
* Unknown event types are logged as warnings.

14. Running the Application

15. Place events.txt in the resources folder.

16. Run Main.java.

17. Events are read, processed, and orders updated in memory.

18. Final order state is printed using CommonUtil.prettyPrintHistory().

19. Example Flow
    Input Events:
    {"eventId":"e1","timestamp":"2025-07-29T10:00:00Z","eventType":"OrderCreated","orderId":"ORD001","customerId":"CUST001","items":\[{"itemId":"P001","qty":2}],"totalAmount":100.00}
    {"eventId":"e2","timestamp":"2025-07-29T11:00:00Z","eventType":"PaymentReceived","orderId":"ORD001","amountPaid":100.00}

Processing Steps:

1. OrderCreatedEvent → create order ORD001 with PENDING status.

2. PaymentReceivedEvent → update status to PAID.

3. Notify observers: LoggerObserver logs status change, AlertObserver sends alert for PAID status.

4. Key Features

  * Event-driven design with polymorphic event deserialization.
  * Observer pattern for notifications.
  * In-memory repository with customer grouping.
  * JSON file-based ingestion.
  * Handles errors gracefully.

12. Libraries Used

  * Jackson Databind
  * Jackson Datatype JSR310 (for Java 8 time Instant)
  * Lombok (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor)
Note: This implementation is a complete working example of an event-driven system suitable for evaluation.

// hatchling
