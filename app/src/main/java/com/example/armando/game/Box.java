package com.example.armando.game;

public class Box {
    public float xmin, ymin, xmax, ymax, width, height;
    public Box(float xmin, float ymin, float xmax, float ymax)
    {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.width = xmax - xmin;
        this.height = ymax - ymin;
    }
}