package com.example.mfaella.physicsapp;

public enum Tag {
    MOVING(1),
    VISIBLE(1 << 1),
    INVINCIBLE(1 << 2),
    SELECTED(1 << 3),
    ROPE_SEGMENT(1 << 4);

    private final int mask;

    Tag(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }
}