package com.sad.function.loaders;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sad.function.global.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JsonLoader {
    private static final Logger logger = LogManager.getLogger(JsonLoader.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public static void load() {
        HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();
        try {
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

            Assets assets = mapper.readValue(new File("input/assets.json"), Assets.class);


            assets.atlases.forEach(atlas -> {
                //Load the texture atlas into the asset manager.
                Global.assetManager.load(atlas.getFile(), TextureAtlas.class);

                //Finish loading the atlas into the asset manager.
                Global.assetManager.finishLoadingAsset(atlas.getFile());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
