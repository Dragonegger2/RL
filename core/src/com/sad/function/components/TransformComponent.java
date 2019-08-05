package com.sad.function.components;


import com.artemis.Component;
import com.sad.function.collision.data.Transform;

/**
 * Used for transforming an object. If you translate
 */
public class TransformComponent extends Component {
    public Transform transform;

    public TransformComponent() {
        transform = new Transform();
    }
}
