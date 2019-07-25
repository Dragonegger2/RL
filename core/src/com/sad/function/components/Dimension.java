package com.sad.function.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Dimension extends Component {
    public float width = 1f;
    public float height = 1f;

    public Vector2 renderOffset;

    public Dimension() {
        renderOffset = new Vector2();
    }
    public Dimension setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Dimension setHeight(float height) {
        this.height = height;
        return this;
    }

    public Dimension setWidth(float width) {
        this.width = width;
        return this;
    }

    public Dimension setRenderOffset(Vector2 renderOffset) {
        this.renderOffset.set(renderOffset);
        return this;
    }
}
