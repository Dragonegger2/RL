package com.sad.function.components;

import com.artemis.Component;
import com.artemis.annotations.DelayedComponentRemoval;

/**
 * Component that describes where this entity needs to be rendered.
 */
@DelayedComponentRemoval        //TODO Add this elsewhere.
public class Layer extends Component {
    public float yOffset = 0.0f;                                //Changes what "layer" this gets rendered at.
    public RENDERABLE_LAYER layer = RENDERABLE_LAYER.DEFAULT;

    //Set this on creation and then don't change it.
    public enum RENDERABLE_LAYER {
        GROUND,
        GROUND_DECALS,
        DEFAULT, //Where most entities go.
        WEATHER,
        UI
    }
}
