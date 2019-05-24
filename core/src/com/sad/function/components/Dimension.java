package com.sad.function.components;

import com.artemis.Component;

public class Dimension extends Component {
    public float width = 1f;
    public float height = 1f;

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
