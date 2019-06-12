package com.sad.function.physics;

import com.artemis.Component;
import com.sad.function.system.cd.shapes.Shape;

public class Collidable extends Component {
    public Type type;

    //TODO have the body assign this object as a parent to retrieve data from.
    public Shape body;


    public enum Type {
        GROUND,
        PLAYER
    }
}
