package com.sad.function.loaders;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimationSequence {
    @JsonProperty("playMode")
    private Animation.PlayMode playMode;
    @JsonProperty("frameDuration")
    private float frameDuration;

    public AnimationSequence() {
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
    }

    public float getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
    }
}
