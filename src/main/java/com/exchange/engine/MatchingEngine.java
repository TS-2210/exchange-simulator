package com.exchange.engine;
import com.exchange.model.Order;
import com.exchange.model.OrderSide;
import com.exchange.model.OrderType;
import com.exchange.model.Trade;
import com.exchange.orderbook.OrderBook;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;
import com.exchange.event.EventBus;
import com.exchange.model.TradeEvent;
public class MatchingEngine {
    private final OrderBook orderBook;
    private final ReentrantLock lock = new ReentrantLock();
    private final EventBus eventBus;
    public MatchingEngine(OrderBook orderBook, EventBus eventBus) {
        this.orderBook = orderBook;
        this.eventBus = eventBus;
    }

    public List<Trade> submitOrder(Order incomingOrder) {
        lock.lock();
        try{
            List<Trade> trades = new ArrayList<>();
            if (incomingOrder.getType() == OrderType.MARKET) {
                matchMarketOrder(incomingOrder, trades);
            }
            else {
                matchLimitOrder(incomingOrder, trades);
            }
            return trades;
        }
        finally {
            lock.unlock();
        }
    }

    private void matchLimitOrder(Order incomingOrder, List<Trade> trades) {
        while (!incomingOrder.isFilled()) {
            Order oppositeOrder = incomingOrder.getSide() == OrderSide.BUY ? orderBook.getBestAsk() : orderBook.getBestBid();
            if (oppositeOrder == null) {
                break;
            }
            if (!pricesCross(incomingOrder, oppositeOrder)) {
                break;
            }
            executeTrade(incomingOrder, oppositeOrder, trades);
        }
        if (!incomingOrder.isFilled()) {
            orderBook.addOrder(incomingOrder);
        }
    }

    private boolean pricesCross(Order incomingOrder, Order oppositeOrder) {
        if (incomingOrder.getSide() == OrderSide.BUY) {
            return incomingOrder.getPrice().compareTo(oppositeOrder.getPrice()) >= 0;
        }
        else {
            return incomingOrder.getPrice().compareTo(oppositeOrder.getPrice()) <= 0;
        }
    }

    private void executeTrade(Order incomingOrder, Order restingOrder, List<Trade> trades) {
        int quantity = Math.min(incomingOrder.getRemainingQuantity(), restingOrder.getRemainingQuantity());
        BigDecimal executionPrice = restingOrder.getPrice();
        Order buyOrder;
        Order sellOrder;
        if (incomingOrder.getSide() == OrderSide.BUY) {
            buyOrder = incomingOrder;
            sellOrder = restingOrder;
        } else {
            buyOrder = restingOrder;
            sellOrder = incomingOrder;
        }
        Trade trade = new Trade(buyOrder, sellOrder, executionPrice, quantity);
        eventBus.publish(new TradeEvent(trade));
        incomingOrder.reduceQuantity(quantity);
        restingOrder.reduceQuantity(quantity);
        trades.add(trade);
        if (restingOrder.isFilled()) {
            orderBook.removeOrder(restingOrder);
        }
    }

    private void matchMarketOrder(Order incomingOrder, List<Trade> trades) {
        while (!incomingOrder.isFilled()) {
            Order oppositeOrder = incomingOrder.getSide() == OrderSide.BUY ? orderBook.getBestAsk() : orderBook.getBestBid();
            if (oppositeOrder == null) {
                break;
            }
            executeTrade(incomingOrder, oppositeOrder, trades);
        }
    }
    public boolean cancelOrder(UUID orderId) {
        lock.lock();
        try{
            return orderBook.cancelOrder(orderId);
    
        }
        finally{
            lock.unlock();
        }
    }

    public boolean changeAmount (UUID orderId, int newAmount) {
        Order order = orderBook.getOrder(orderId);
        if (order == null) {
            return false;
        }
        if (newAmount <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        order.setRemainingQuantity(newAmount);
        return true;
    }
}
