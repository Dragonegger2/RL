package com.sad.function.command;

import com.badlogic.ashley.core.Entity;

public interface Command {
    void execute(Entity entity);
}
