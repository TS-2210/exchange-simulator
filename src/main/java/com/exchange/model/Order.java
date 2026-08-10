package com.exchange.model;

import java.time.Instant;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String traderId;
    private final String symbol;
    private final OrderSide side;
    private final OrderType type;
    private final double price;
    private final int originalQuantity;
    private int remainingQuantity;
    private final Instant timestamp;
    public Order(
            String traderId,
            String symbol,
            OrderSide side,
            OrderType type,
            double price,
            int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        this.id = UUID.randomUUID();
        this.traderId = traderId;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.price = price;
        this.originalQuantity = quantity;
        this.remainingQuantity = quantity;
        this.timestamp = Instant.now();
    }

    public void reduceQuantity(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException();
        if (amount > remainingQuantity)
            throw new IllegalArgumentException();
        remainingQuantity -= amount;
    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    public int getExecutedQuantity() {
        return originalQuantity - remainingQuantity;
    }

    public double getPrice(){
        return price;
    }

    public OrderSide getSide(){
        return side;
    }
}