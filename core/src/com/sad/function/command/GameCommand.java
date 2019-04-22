package com.sad.function.command;

import com.badlogic.ashley.core.Entity;

public interface GameCommand {
    void execute(Entity entity, float delta);
}
