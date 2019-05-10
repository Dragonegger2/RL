package com.sad.function.loaders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Atlas {
    @JsonProperty("file")
    private String file;
    @JsonProperty("animationSequences")
    private
    HashMap<String, AnimationSequence> animationSequences;

    public Atlas() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public HashMap<String, AnimationSequence>  getAnimationSequences() {
        return animationSequences;
    }

    public void setAnimationSequences(HashMap<String, AnimationSequence>  animationSequences) {
        this.animationSequences = animationSequences;
    }
}
