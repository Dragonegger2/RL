package com.sad.function.command;

import com.artemis.World;

public interface GameCommand {
    void execute(World world, int entity);
}
