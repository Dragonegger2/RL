package com.sad.function.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
    private static final float IMPULSE = 2.5f;
    private static final float VERTICAL_IMPULSE = 60.0f;
    private static final float MAX_HORIZONTAL_VELOCITY = 5f;
    private static final float HORIZONTAL_DRAG = .7f;           // .7 = 30% drag
    private ComponentMapper<Animation> mAnimation;
    private ComponentMapper<PhysicsBody> mPhysics;
    private ComponentMapper<PlayerComponent> mPlayer;
    private Vector2 lVelocity;

    @Override
    protected void processSystem() {
        processEntity(GameInfo.PLAYER); //TODO Just combine these methods.
    }

    private void processEntity(int entity) {
        lVelocity = mPhysics.create(entity).body.getLinearVelocity();
        Body player = mPhysics.create(entity).body;

        //CAP HORIZONTAL VELOCITY
        if (Math.abs(lVelocity.x) > MAX_HORIZONTAL_VELOCITY) {
            lVelocity.x = Math.signum(lVelocity.x) * MAX_HORIZONTAL_VELOCITY;
            mPhysics.create(entity).body.setLinearVelocity(lVelocity.x, lVelocity.y);
        }

        //Calculate stilltime and damp
        if (!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)) {
            //Dampen the linear lVelocity in the X-direction by 10%, kill speed if it no longer exceeds 50%
            player.setLinearVelocity(lVelocity.x * HORIZONTAL_DRAG, lVelocity.y);

            mAnimation.create(entity).animationName = "hero-male-side-idle";
        }

        //APPLY LEFT IMPULSE.
        if (Gdx.input.isKeyPressed(Keys.LEFT) && lVelocity.x > -MAX_HORIZONTAL_VELOCITY) {
//            KeyActionBindings.actions.get(Action.MOVE_LEFT).execute(world, entity);
            player.applyLinearImpulse(-IMPULSE, 0.0f, player.getWorldCenter().x, player.getWorldCenter().y, true);
            mAnimation.create(entity).animationName = "hero-male-side-walk";
            mAnimation.create(entity).direction = Animation.Direction.LEFT;
        }

        //APPLY RIGHT IMPULSE.
        if (Gdx.input.isKeyPressed(Keys.RIGHT) && lVelocity.x < MAX_HORIZONTAL_VELOCITY) {
            player.applyLinearImpulse(IMPULSE, 0.0f, player.getWorldCenter().x, player.getWorldCenter().y, true);

            mAnimation.create(entity).animationName = "hero-male-side-walk";
            mAnimation.create(entity).direction = Animation.Direction.RIGHT;
        }


        if (Gdx.input.isKeyPressed(Keys.UP) && GameInfo.FOOT_CONTACTS >= 1) { //Only allow jumping when they're on the ground.
            KeyActionBindings.actions.get(Action.MOVE_UP).execute(world, entity);
            player.applyForce(0f, player.getMass() * VERTICAL_IMPULSE, player.getWorldCenter().x, player.getWorldCenter().y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            KeyActionBindings.actions.get(Action.QUIT_GAME).execute(world, entity);
        }
    }

}
