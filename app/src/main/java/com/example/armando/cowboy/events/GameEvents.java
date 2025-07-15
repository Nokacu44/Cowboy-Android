package com.example.armando.cowboy.events;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class GameEvents {

    public enum EventType {
        GAME_STARTED,
        BEGIN_AIM,
        END_AIM,
        SHOOT,
        BANG_BUTTON_PRESSED,
        ROPE_CUT,
        HANGMAN_DEAD,
        OUT_OF_AMMO,
        TIMEOUT,
        GAME_FINISHED
    }

    private final Map<EventType, List<Consumer<GameEventData>>> eventListeners;
    private final GameEventData eventData;

    public GameEvents() {
        eventListeners = new EnumMap<>(EventType.class);
        for (EventType event : EventType.values()) {
            eventListeners.put(event, new ArrayList<>());
        }
        eventData = new GameEventData();
    }

    public void connect(EventType event, Consumer<GameEventData> listener) {
        Objects.requireNonNull(eventListeners.get(event)).add(listener);
    }

    public void disconnect(EventType event, Consumer<GameEventData> listener) {
        Objects.requireNonNull(eventListeners.get(event)).remove(listener);
    }

    public void clearListeners() {
        for (List<Consumer<GameEventData>> listeners : eventListeners.values()) {
            listeners.clear();
        }
    }

    public void emit(EventType event, Object... data) {
        eventData.setData(data);
        List<Consumer<GameEventData>> listeners = eventListeners.get(event);
        if (listeners != null) {
            for (Consumer<GameEventData> listener : listeners) {
                listener.accept(eventData);
            }
        }
    }
}
