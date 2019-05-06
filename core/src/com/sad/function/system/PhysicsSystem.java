package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sad.function.components.Physics;
import com.sad.function.components.Position;
import com.sad.function.components.Velocity;

public class PhysicsSystem extends IteratingSystem {
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    private final ComponentMapper<Velocity> velocity = ComponentMapper.getFor(Velocity.class);
    private final ComponentMapper<Physics> physics = ComponentMapper.getFor(Physics.class);

    public PhysicsSystem() {
        super(Family.all(Position.class, Velocity.class, Physics.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position position = this.position.get(entity);
        Velocity velocity = this.velocity.get(entity);
//        Physics physics = this.physics.get(entity);

        //Drag coefficient of the surface we're in contact with.
        float k = 0;
        //Mass of the object in question.

        //Apply Drag.
        //velocity.xVelocity = (float) (velocity.xVelocity - k * velocity.xVelocity - m * Math.pow(velocity.xVelocity, 2));

//        velocity.xVelocity = (float) (velocity.xVelocity - k * velocity.xVelocity - physics.mass * Math.pow(velocity.xVelocity,2));
//        velocity.yVelocity = (float) (velocity.yVelocity - k * velocity.yVelocity - physics.mass * Math.pow(velocity.yVelocity,2));
        float movementX = velocity.xVelocity - k * velocity.xVelocity;
        float movementY = velocity.yVelocity - k * velocity.yVelocity;
        position.x += movementX;
        position.y += movementY;

    }
}
