package com.sad.function.components;


import com.artemis.Component;
import com.sad.function.collision.data.Transform;

/**
 * Used for transforming an object. Not sure how I'm going to be binding these to
 * something like a {@link PhysicsComponent}.
 */
public class TransformComponent extends Component {
    public Transform transform;
}
