package com.sad.function.loaders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Atlas {
    @JsonProperty("animations")
    private List<AnimationSequence> animations;

    @JsonProperty("static")
    private List<String> staticResources;
    
    public Atlas() {
    }

    public List<AnimationSequence> getAnimations() {
        return animations;
    }

    public List<String> getStaticResources() {
        return staticResources;
    }
}
