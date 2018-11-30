package com.sad.function.global;

import com.badlogic.ashley.core.Engine;

public class Global {
    private static Engine engine = new Engine();
    private static Textures textures = new Textures();

    public static Engine engine() { return engine; }

    public static Textures textures() { return textures; }
}
