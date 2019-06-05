package com.sad.function.system.cd.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Simplex {
    ArrayList<Vector2> simplex;

    public Simplex() {
        simplex = new ArrayList<>();
    }

    public Vector2 getA() {
        return simplex.get(simplex.size() - 1);
    }

    public Vector2 getB() {
        return simplex.get(simplex.size() - 2);
    }

    public Vector2 getC() {
        return simplex.get(simplex.size() - 3);
    }

    public void remove(Vector2 removeTarget) {
        simplex.remove(removeTarget);
    }

    public void add(Vector2 v) {
        simplex.add(v);
    }

    public Vector2 getLast() {
        return simplex.get(simplex.size() - 1);
    }

    public int size() {
        return simplex.size();
    }

    public ArrayList<Vector2> get() {
        return simplex;
    }

    public Vector2 get(int index) {
        return simplex.get(index);
    }
}
