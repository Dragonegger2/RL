package com.sad.function.components;

import com.artemis.Component;

public class Animation extends Component {
    public float stateTime = 0;
    public boolean looping = true;
    public String animationName = "hero-male-front-idle";

    public Direction direction = Direction.DOWN;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

}
