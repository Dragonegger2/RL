package com.sad.function.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Holds the data for my box2d collision detection.
 *
 * The height and width are the size of the body.
 */
public class PhysicsBody extends Component {
    public Body body;

    public float height;
    public float width;

    /**
     * Box2D uses origin/center based coordinates. I dont' use that anywhere else.
     *
     * @return lower-left origin based x coordinate.
     */
    public float getPositionX() {
        return body.getPosition().x - width / 2;
    }

    public float getPositionY() {
        return body.getPosition().y - height / 2;
    }
}
