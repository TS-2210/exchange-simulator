package com.exchange.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.math.BigDecimal;

public class Order {
    private final UUID id;
    private final String traderId;
    private final String symbol;
    private final OrderSide side;
    private final OrderType type;
    private final BigDecimal price;
    private final int originalQuantity;
    private int remainingQuantity;
    private final Instant timestamp;
    private OrderStatus status;
    public Order(
            String traderId,
            String symbol,
            OrderSide side,
            OrderType type,
            BigDecimal price,
            int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.id = UUID.randomUUID();
        this.traderId = traderId;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.price = price;
        this.originalQuantity = quantity;
        this.remainingQuantity = quantity;
        this.timestamp = Instant.now();
        this.status = OrderStatus.NEW;
    
    }

    public void reduceQuantity(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException();
        if (amount > remainingQuantity)
            throw new IllegalArgumentException();
        remainingQuantity -= amount;
        if (remainingQuantity == 0) {
            status = OrderStatus.FILLED;
        } 
        else {
            status = OrderStatus.PARTIALLY_FILLED;
        }
    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    public int getExecutedQuantity() {
        return originalQuantity - remainingQuantity;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public OrderSide getSide(){
        return side;
    }

    public UUID getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderType getType(){
        return type;
    }

    public int getRemainingQuantity(){
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQty) {
        if(remainingQty < 0 || remainingQty > originalQuantity){
            throw new IllegalArgumentException("Remaining qty should be between 0 and original amount");
        }
        this.remainingQuantity= remainingQty;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}