package com.sad.function.components;

public class UserData {
    public int id = -1;
    public ObjectType type = ObjectType.GROUND;

    public enum ObjectType {
        PLAYER,
        WALL,
        GROUND
    }
}
