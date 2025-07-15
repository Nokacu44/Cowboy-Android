package com.example.armando.game.events;

public class GameEventData {
    private Object[] data;

    public void setData(Object... data) {
        this.data = data;
    }

    public <T> T get(int index, Class<T> type) {
        return type.cast(data[index]);
    }
}
