package ru.myitschool.dcrawler.event;

import java.util.HashMap;

public abstract class EventListener {

    public EventListener() throws Exception {
        for (String code : eventCodes()) {
            EventController.registerEvent(code).setListener(this);
        }
    }

    protected abstract HashMap<String, Object> fire(String eventCode, HashMap<String, Object> args);

    public abstract String[] eventCodes();

    public static void initListeners() {
        try {
            new EntityEventListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
