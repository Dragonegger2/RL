package com.sad.function.loaders;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Assets {
    @JsonProperty("atlas")
    public List<Atlas> atlases;

    public Assets() {
    }
}