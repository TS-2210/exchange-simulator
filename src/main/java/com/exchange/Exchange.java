package com.exchange;
import com.exchange.engine.MatchingEngine;
import com.exchange.model.Order;
import com.exchange.model.Trade;
import com.exchange.orderbook.OrderBook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exchange {
    private final Map<String, MatchingEngine> engines = new HashMap<>();

    public void registerTickerSymbol(String symbol) {
        if (engines.containsKey(symbol)) {
            throw new IllegalArgumentException("Ticker already registered: " + symbol);
        }
        OrderBook orderBook = new OrderBook();
        MatchingEngine matchingEngine = new MatchingEngine(orderBook);
        engines.put(symbol, matchingEngine);
    }

    public List<Trade> submitOrder(Order order) {
        MatchingEngine engine = engines.get(order.getSymbol());
        if (engine == null) {
            throw new IllegalArgumentException("Invalid ticker: " + order.getSymbol());
        }
        return engine.submitOrder(order);
    }
}