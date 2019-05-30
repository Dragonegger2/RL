package com.sad.function.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.components.Animation;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.PlayerComponent;
import com.sad.function.global.GameInfo;
import com.sad.function.input.KeyActionBindings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@All
public class PlayerInputSystem extends BaseSystem {
    private static final Logger logger = LogManager.getLogger(PlayerInputSystem.class);

    private ComponentMapper<Animation> mAnimation;
    private ComponentMapper<PhysicsBody> mPhysics;
    private ComponentMapper<PlayerComponent> mPlayer;

    private static final float MAX_HORIZONTAL_VELOCITY = 5f;
    private static final float HORIZONTAL_DRAG = .9f;

    @Override
    protected void processSystem() {
        processEntity(GameInfo.PLAYER); //TODO Just combine these methods.
    }

    private Vector2 linearVelocity;

    private void processEntity(int entity) {

        linearVelocity = mPhysics.create(entity).body.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            KeyActionBindings.actions.get(Action.MOVE_LEFT).execute(world, entity);
            world.getMapper(Animation.class).create(entity).animationName = "hero-male-side-walk";
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.LEFT;
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            KeyActionBindings.actions.get(Action.MOVE_RIGHT).execute(world, entity);
            world.getMapper(Animation.class).create(entity).animationName = "hero-male-side-walk";
            world.getMapper(Animation.class).create(entity).direction = Animation.Direction.RIGHT;
        }

        if(!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)) {
            //Dampen the linear velocity in the X-direction by 10%, kill speed if it no longer exceeds 50%

        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            KeyActionBindings.actions.get(Action.MOVE_UP).execute(world, entity);
        }

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            KeyActionBindings.actions.get(Action.QUIT_GAME).execute(world, entity);
        }
    }

}
