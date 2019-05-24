package com.sad.function.factory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Factory {

    public static class BodyCreator {

        private BodyDef.BodyType bodyType;
        private Vector2 position;
        private boolean fixedRotation;

        public BodyCreator() {
            position = new Vector2();
        }

        public BodyCreator setBodyType(BodyDef.BodyType bodyType) {
            this.bodyType = bodyType;
            return this;
        }

        public BodyCreator setPosition(float x, float y) {
            this.position.set(x, y);
            return this;
        }

        public BodyCreator hasFixedRotation(boolean fixedRotation) {
            this.fixedRotation = fixedRotation;
            return this;
        }

        public BodyFactory buildBody(com.badlogic.gdx.physics.box2d.World pWorld) {
            BodyDef bodyDef = new BodyDef();

            bodyDef.fixedRotation = fixedRotation;
            bodyDef.position.set(position);
            bodyDef.type = bodyType;


            return new BodyFactory(pWorld.createBody(bodyDef));
        }

        public class BodyFactory {
            private Body body;

            public BodyFactory(Body body) {
                this.body = body;
            }

            public BodyFactory createBoxFixture(float width, float height, float density, float friction) {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(width, height);

                Fixture hitbox = body.createFixture(shape, density);
                hitbox.setFriction(friction);
                //TODO: Update friction in circle.
                shape.dispose();

                return this;
            }

            public BodyFactory createCircleFixture(float radius, float density) {
                Shape shape = new CircleShape(); 
                shape.setRadius(radius);
                body.createFixture(shape, density).setFriction(0.7f);

                return this;
            }

            public Body getBody() {
                return body;
            }
        }
    }

}
