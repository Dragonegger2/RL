package com.sad.function.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sad.function.loaders.AnimationSequence;
import com.sad.function.loaders.Atlas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ResourceManager {
    private static final Logger logger = LogManager.getLogger(ResourceManager.class);
    private static final String res = "packed/resources.atlas";
    private ObjectMapper mapper;
    private TextureRegion nullTexture;
    private TextureAtlas resources;
    private HashMap<String, Animation<TextureRegion>> animationList;
    private HashMap<String, TextureRegion> staticResources;

    public ResourceManager() {

        mapper = new ObjectMapper();
        animationList = new HashMap<>();
        staticResources = new HashMap<>();

        resources = new TextureAtlas(Gdx.files.internal(res)); //Actually load it into the global asset manager.

        try {
            Atlas atlas = mapper.readValue(new File("input/assets.json"), Atlas.class);

            logger.info("Loaded game resources from {}.", res);

            for (AnimationSequence animation : atlas.getAnimations()) {
                animationList.put(animation.getName().toLowerCase(),
                        new Animation<>(
                                animation.getFrameDuration(),
                                resources.findRegions(animation.getName()),
                                animation.getPlayMode()));

                logger.info("Created new animation: {} {} {}",
                        animation.getName(),
                        animation.getFrameDuration(),
                        animation.getPlayMode());
            }

            for( String staticResourceName : atlas.getStaticResources() ) {
                staticResources.put(staticResourceName, resources.findRegion(staticResourceName));

                logger.info("Created new static resource: {}", staticResourceName);
            }
        } catch (IOException e) {
            logger.error("Unable to load animations from asset list: {}", "input/assets.json");
        }
    }

    /**
     *
     * @param animationName name of the animation to get a key frame for.
     * @param elapsedTime, time spent running this animation.
     * @param looping, whether we loop the animation if we surpass it's frame duration.
     * @return the expected animation frame OR the null texture if nothing can be found.
     */
    public TextureRegion getAnimationKeyFrame(String animationName, float elapsedTime, boolean looping) {
        if (animationList.containsKey(animationName.toLowerCase())) {
            return animationList.get(animationName.toLowerCase()).getKeyFrame(elapsedTime, looping);
        }

        return staticResources.get("null");
    }

    public TextureRegion getStaticAsset(String assetName) {
        if(staticResources.containsKey(assetName)) {
            return staticResources.get(assetName);
        }

        return staticResources.get("null");
    }
    public void dispose() {
        resources.dispose();
    }
}