package com.sad.function.components;

import com.artemis.Component;

public class Dimension extends Component {
    public float width = 32;
    public float height = 32;

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
