package ru.myitschool.dcrawler.event;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class EventController {

    private static Array<EventAction> beforeActions = new Array<>();
    private static Array<EventAction> afterActions = new Array<>();

    private static HashMap<String, EventSlot> events = new HashMap<>();

    public static HashMap<String, Object> callEvent(String eventCode, HashMap<String, Object> args){
        beforeEvent(eventCode);
        HashMap<String, Object> result = event(eventCode, args);
        afterEvent(eventCode);
        return result;
    }

    private static void beforeEvent(String eventCode){
        for (EventAction action : beforeActions){
            action.act(eventCode);
        }
    }

    private static HashMap<String, Object> event(String eventCode, HashMap<String, Object> args){
        return events.get(eventCode).fire(eventCode, args);
    }

    private static void afterEvent(String eventCode){
        for (EventAction action : afterActions){
            action.act(eventCode);
        }
    }

    public static EventSlot registerEvent(String eventCode) throws Exception {
        if (!events.containsKey(eventCode)) {
            EventSlot slot = new EventSlot();
            events.put(eventCode, slot);
            return slot;
        } else {
            throw new Exception("Event with code " + eventCode + " already registered");
        }
    }

    public static void addActionBeforeEvent(EventAction action){
        beforeActions.add(action);
    }

    public static void addActionAfterEvent(EventAction action){
        afterActions.add(action);
    }
}
