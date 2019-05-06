package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.sad.function.components.Physics;
import com.sad.function.components.Position;
import com.sad.function.components.Velocity;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class PhysicsSystem extends IteratingSystem {
    private final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    private final ComponentMapper<Velocity> velocity = ComponentMapper.getFor(Velocity.class);
    private final ComponentMapper<Physics> physics = ComponentMapper.getFor(Physics.class);

    public PhysicsSystem() {
        super(Family.all(Position.class, Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        Position position = this.position.get(entity);
        Velocity velocity = this.velocity.get(entity);

        Gdx.graphics.setTitle(String.format("X Velocity: %s Y Velocity: %s", velocity.x, velocity.y));

        float drag = 5f;

        velocity.x = velocity.x * (1 - delta * drag);
        velocity.y = velocity.y * (1 - delta * drag);

        position.x += velocity.x;
        position.y += velocity.y;
    }
}
