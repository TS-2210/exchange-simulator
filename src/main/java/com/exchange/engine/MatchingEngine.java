package com.exchange.engine;
import com.exchange.model.Order;
import com.exchange.model.OrderSide;
import com.exchange.model.OrderType;
import com.exchange.model.Trade;
import com.exchange.orderbook.OrderBook;
import java.util.ArrayList;
import java.util.List;
public class MatchingEngine {
    private final OrderBook orderBook;

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public List<Trade> submitOrder(Order incomingOrder) {
        List<Trade> trades = new ArrayList<>();
        if (incomingOrder.getType() == OrderType.MARKET) {
            matchMarketOrder(incomingOrder, trades);
        }
        else {
            matchLimitOrder(incomingOrder, trades);
        }
        return trades;
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
            return incomingOrder.getPrice() >= oppositeOrder.getPrice();
        }
        else {
            return incomingOrder.getPrice() <= oppositeOrder.getPrice();
        }
    }

    private void executeTrade(Order incomingOrder, Order restingOrder, List<Trade> trades) {
        int quantity = Math.min(incomingOrder.getRemainingQuantity(), restingOrder.getRemainingQuantity());
        double executionPrice = restingOrder.getPrice();
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
}
