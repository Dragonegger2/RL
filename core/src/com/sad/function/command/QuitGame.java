package com.sad.function.command;

import com.artemis.World;
import com.badlogic.gdx.Gdx;

public class QuitGame implements GameCommand {

    @Override
    public void execute(World world, int entity, float delta) {
        Gdx.app.exit();
    }

}
