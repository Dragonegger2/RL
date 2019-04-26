package com.sad.function.command.movement;

import com.badlogic.ashley.core.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sad.function.components.Position;

@JsonRootName("move-left")
public class MoveLeft implements GameCommand {
    @JsonProperty("x")
    private float xVelocity;

    public MoveLeft() { }
    public MoveLeft(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    @Override
    public void execute(Entity entity, float delta) {
        Position position = entity.getComponent(Position.class);

        if(position != null) {
            position.x -= xVelocity * delta;
        }
    }
}
