package ru.myitschool.dcrawler.event;

import java.util.HashMap;

public class EventSlot {

    private EventListener listener;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    HashMap<String, Object> fire(String eventCode, HashMap<String, Object> args){
        return listener != null? listener.fire(eventCode, args) : null;
    }
}
