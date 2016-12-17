package ru.rep1.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lshi on 23.11.2016.
 */
public class EventBus {
    private static class SingletonHolder {
        public static EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Map<String, List<Runnable>> listeners = new HashMap<>();

    public synchronized void subscribe(String message, Runnable action) {
        if (listeners.containsKey(message)) {
            listeners.get(message).add(action);
        } else {
            List<Runnable> actions = new LinkedList<>();
            actions.add(action);
            listeners.put(message, actions);
        }
    }

    public void unsubscribe(String message, Runnable actor) {
        if (listeners.containsKey(message)) {
            listeners.get(message).remove(actor);
        }
    }

    public void publish(String message) {
        System.out.println("Get event " + message);
        if (listeners.containsKey(message)) {
            for (Runnable actor : listeners.get(message)) {
                actor.run();
            }
        }
    }
}
