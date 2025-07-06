package com.example.mfaella.physicsapp.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class GameEvents {
    public enum EventType {
        GAME_STARTED,
        BEGIN_AIM,
        END_AIM,
        SHOOT,
        ROPE_CUT,
    }

    private static final Map<EventType, List<Consumer<GameEventData>>> eventListeners = new HashMap<>();
    private static final GameEventData eventData = new GameEventData();

    static {
        for (EventType event : EventType.values()) {
            eventListeners.put(event, new ArrayList<>());
        }
    }

    public static void connect(EventType event, Consumer<GameEventData> listener) {
        Objects.requireNonNull(eventListeners.get(event)).add(listener);
    }

    public static void disconnect(EventType event, Consumer<GameEventData> listener) {
        Objects.requireNonNull(eventListeners.get(event)).remove(listener);
    }

    public static void clearListeners() {
        eventListeners.clear();
    }

    public static void emit(EventType event, Object... data) {
        eventData.setData(data);
        if (eventListeners.containsKey(event)) {
            for (Consumer<GameEventData> listener : Objects.requireNonNull(eventListeners.get(event))) {
                listener.accept(eventData);
            }
        }
    }
}
