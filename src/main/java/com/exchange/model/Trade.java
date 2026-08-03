package com.exchange.model;

public class Trade {
    private final long buyOrderId;
    private final long sellOrderId;
    private final double price;
    private final long quantity;

    public Trade(long buyOrderId,
                 long sellOrderId,
                 double price,
                 long quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    public long getBuyOrderId() {
        return buyOrderId;
    }

    public long getSellOrderId() {
        return sellOrderId;
    }

    public double getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "TRADE -> BUY " +
                buyOrderId +
                " SELL " +
                sellOrderId +
                " PRICE=" +
                price +
                " QTY=" +
                quantity; 
    }
}