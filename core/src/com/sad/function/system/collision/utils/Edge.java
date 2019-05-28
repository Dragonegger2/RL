package com.sad.function.system.collision.utils;

import com.badlogic.gdx.math.Vector2;

public class Edge {
    public double distance;
    public Vector2 normal;
    public int index;

    public Edge() {

    }
    public Edge(double distance, Vector2 normal, int index) {
        this.distance = distance;
        this.normal = normal;
        this.index = index;
    }
}
