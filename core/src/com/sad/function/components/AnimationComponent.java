package com.sad.function.components;

import com.badlogic.ashley.core.Component;
import com.sad.function.common.Observer;
import com.sad.function.event.Event;
import com.sad.function.event.EventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnimationComponent extends com.artemis.Component implements Observer, Component {
    private static final Logger logger = LogManager.getLogger(AnimationComponent.class);

    public float stateTime = 0f;
    public boolean looping = false;
    public String animationName = "NULL_ANIMATION.jpg";

    @Override
    public void onNotify(Event event) {
        if(event.getEventType() == EventType.WALKING_LEFT) {
            //
            logger.info("Changing animation to walk left.");

            //Clear state time.
            stateTime = 0;
        }
    }

//    TODO Create a renderable Component that is a mishmash of both a static and a non-static component. Slash it has an object a single method but differentiates between the two somehow. IE
/*
class thing {
    public RenderableInformation renderableInformation;

    public TextureAtlas or whatever getRenderable() {
//        use renderableInformation to return a single frame to render.
    }
}
 */
}
