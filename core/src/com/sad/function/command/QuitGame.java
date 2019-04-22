package com.sad.function.command;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.sad.function.components.Position;

public class QuitGame implements GameCommand {
    @Override
    public void execute(Entity entity, float delta) {
        Gdx.app.exit();
    }
}
