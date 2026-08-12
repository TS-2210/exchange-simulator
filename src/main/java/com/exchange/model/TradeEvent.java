package com.exchange.model;

public class TradeEvent implements Event {
    private final Trade trade;
    
    public TradeEvent(Trade trade) {
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }    
}
