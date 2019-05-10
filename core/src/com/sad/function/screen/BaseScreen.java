package com.sad.function.screen;

import com.badlogic.gdx.graphics.Camera;

public abstract class BaseScreen {
    public abstract void enter();
    public abstract void exit();

    public abstract Camera getCamera();
}