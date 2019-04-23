package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sad.function.components.Position;
import com.sad.function.components.Texture;
import com.sad.function.components.Velocity;
import com.sad.function.global.Global;

import java.util.Comparator;

public class PhysicsSystem extends IteratingSystem {
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    private final ComponentMapper<Velocity> velocity = ComponentMapper.getFor(Velocity.class);

    public PhysicsSystem() {
        super(Family.all(Position.class, Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position position = this.position.get(entity);
        Velocity velocity = this.velocity.get(entity);

        //Drag coefficient of the surface we're in contact with.
        float k = 0;
        //Mass of the object in question.
        float m = 100;

        //Apply Drag.
        //velocity.xVelocity = (float) (velocity.xVelocity - k * velocity.xVelocity - m * Math.pow(velocity.xVelocity, 2));


        //Need to do loading of resources.
        position.x += velocity.xVelocity;
        position.y += velocity.yVelocity;

    }
}
