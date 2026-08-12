package com.exchange.event;
import com.exchange.model.Event;

public interface EventListener {
    void onEvent(Event event);
}
