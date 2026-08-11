package com.exchange.model;

import java.time.Instant;
import java.util.UUID;

public class Trade {
    private final UUID id;
    private final UUID buyOrderId;
    private final UUID sellOrderId;
    private final String symbol;
    private final double price;
    private final int quantity;
    private final Instant timestamp;
    public Trade(
            Order buyOrder,
            Order sellOrder,
            double price,
            int quantity) {
        this.id = UUID.randomUUID();
        this.buyOrderId = buyOrder.getId();
        this.sellOrderId = sellOrder.getId();
        this.symbol = buyOrder.getSymbol();
        this.price = price;
        this.quantity = quantity;
        this.timestamp = Instant.now();
    }
    public UUID getId() {
        return id;
    }
    public UUID getBuyOrderId() {
        return buyOrderId;
    }
    public UUID getSellOrderId() {
        return sellOrderId;
    }
    public String getSymbol() {
        return symbol;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
}