package com.sad.function.components;

import com.artemis.Component;
import com.sad.function.systems.PhysicsSystem;
import com.sad.function.collision.Body;

/**
 * Component used in the {@link PhysicsSystem} to calculate collisions.
 */
public class PhysicsBody extends Component {
    public Body body;
}