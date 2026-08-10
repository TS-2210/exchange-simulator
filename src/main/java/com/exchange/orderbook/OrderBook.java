package com.exchange.orderbook;
import com.exchange.model.Order;
import com.exchange.model.OrderSide;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeMap;

public class OrderBook {
    private final TreeMap<Double, Deque<Order>> buyOrders =
            new TreeMap<>();
    private final TreeMap<Double, Deque<Order>> sellOrders =
            new TreeMap<>();

    public void addOrder(Order order) {
        TreeMap<Double, Deque<Order>> book =
                order.getSide() == OrderSide.BUY
                        ? buyOrders
                        : sellOrders;
        book.computeIfAbsent(
                order.getPrice(),
                price -> new ArrayDeque<>());
        book.get(order.getPrice())
                .addLast(order);
    }

    public Order getBestBid() {
        if (buyOrders.isEmpty()) {
            return null;
        }
        return buyOrders
                .lastEntry()
                .getValue()
                .peekFirst();
    }

    public Order getBestAsk() {
        if (sellOrders.isEmpty()) {
            return null;
        }
        return sellOrders
                .firstEntry()
                .getValue()
                .peekFirst();
    }

    public void removeOrder(Order order) {
        TreeMap<Double, Deque<Order>> book =
                order.getSide() == OrderSide.BUY
                        ? buyOrders
                        : sellOrders;
        Deque<Order> queue = book.get(order.getPrice());
        if (queue == null) {
            return;
        }
        queue.remove(order);
        if (queue.isEmpty()) {
            book.remove(order.getPrice());
        }
    }
}