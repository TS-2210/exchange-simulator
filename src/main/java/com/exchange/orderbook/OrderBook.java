package com.exchange.orderbook;
import com.exchange.model.Order;
import com.exchange.model.OrderSide;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.math.BigDecimal;

public class OrderBook {
    private final TreeMap<BigDecimal, Deque<Order>> buyOrders = new TreeMap<>();
    private final TreeMap<BigDecimal, Deque<Order>> sellOrders = new TreeMap<>();
    private final Map<UUID, Order> ordersById = new HashMap<>();

    public void addOrder(Order order) {
        TreeMap<BigDecimal, Deque<Order>> book =
                order.getSide() == OrderSide.BUY
                        ? buyOrders
                        : sellOrders;
        book.computeIfAbsent(
                order.getPrice(),
                price -> new ArrayDeque<>());
        book.get(order.getPrice())
                .addLast(order);
        ordersById.put(order.getId(), order);
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
        TreeMap<BigDecimal, Deque<Order>> book =
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
        ordersById.remove(order.getId(), order);
    }

    public boolean cancelOrder(UUID orderId) {
        Order order = ordersById.get(orderId);
        if (order == null) return false;
        removeOrder(order);
        return true;
    }
}