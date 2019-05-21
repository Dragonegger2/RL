package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntSet;
import com.sad.function.components.*;
import com.sad.function.global.Global;
import com.sad.function.manager.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class RenderingSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(RenderingSystem.class);
    //    private HashMap<Layer.RENDERABLE_LAYER, Boolean> layerNeedsSorting; TODO Future improvement?
    private ZComparator zComparator;

    private ComponentMapper<Layer> mLayer;
    private ComponentMapper<Position> mPosition;
    private ComponentMapper<TextureComponent> mTexture;
    private ComponentMapper<Dimension> mDimension;
    private ComponentMapper<Animation> mAnimation;
    private ComponentMapper<Collidable> mCollidable;

    private IntMap<IntSet> entitiesPerLayer;
    private IntArray layersToRender;

    private boolean dirty = false;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private ResourceManager resourceManager;
    private Camera camera;
    private HashMap<Layer.RENDERABLE_LAYER, List<Integer>> layerCollections;            //Used for rendering.
    private IntMap<Layer.RENDERABLE_LAYER> idCollection;                                //Used for removed.

    private Layer.RENDERABLE_LAYER[] layers = new Layer.RENDERABLE_LAYER[]{
            Layer.RENDERABLE_LAYER.GROUND,
            Layer.RENDERABLE_LAYER.GROUND_DECALS,
            Layer.RENDERABLE_LAYER.DEFAULT,
            Layer.RENDERABLE_LAYER.WEATHER,
            Layer.RENDERABLE_LAYER.UI
    };

    public RenderingSystem(ResourceManager resourceManager, Camera camera) {
        super(Aspect.all(Position.class, Layer.class).one(TextureComponent.class, Animation.class));

        this.resourceManager = resourceManager;
        this.camera = camera;
        shapeRenderer = new ShapeRenderer();

        entitiesPerLayer = new IntMap<>();
        layersToRender = new IntArray();

        layerCollections = new HashMap<>();
        idCollection = new IntMap<>();

        batch = new SpriteBatch();

        for (Layer.RENDERABLE_LAYER layer : layers) {
            layerCollections.put(layer, new ArrayList<>());
        }
    }

    @Override
    public void begin() {
        if (zComparator == null) {
            zComparator = new ZComparator(getWorld(), 100);
        }
        for (Layer.RENDERABLE_LAYER layer : layerCollections.keySet()) {
            //Going to sort the same way for all of the collections.
            //TODO Sort by y / ratio - yOffset of each entity.
            layerCollections.get(layer).sort(zComparator);
        }
    }

    @Override
    protected void inserted(int entity) {
        if (!layerCollections.containsKey(mLayer.create(entity).layer)) {
            layerCollections.put(mLayer.create(entity).layer, new ArrayList<>());
        }

        layerCollections.get(mLayer.create(entity).layer).add(entity);
        idCollection.put(entity, mLayer.create(entity).layer);
    }

    @Override
    protected void removed(int entity) {
        layerCollections.get(idCollection.get(entity)).remove(entity);
    }

    @Override
    protected void processSystem() {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (Layer.RENDERABLE_LAYER layer : layers) {
            Iterator<Integer> iterator = layerCollections.get(layer).iterator();
            while (iterator.hasNext()) {
                renderEntity(iterator.next());
            }
        }

        batch.end();

        if (Global.DEBUG) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            Iterator<Integer> iterator = layerCollections.get(Layer.RENDERABLE_LAYER.DEFAULT).iterator();
            while (iterator.hasNext()) {
                renderBoundingBox(iterator.next());
            }

            shapeRenderer.end();
        }

    }

    private boolean inView(Camera camera, Position position, float width, float height) {
        return camera.frustum.pointInFrustum(position.x, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + width, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + width, position.y + height, position.z) ||
                camera.frustum.pointInFrustum(position.x, position.y + height, position.z);
    }

    private void renderEntity(int entity) {
        Position pos = mPosition.create(entity);
        Dimension dim = mDimension.create(entity);

        //Check if in view before bothering to render the entity.
        if (inView(camera, pos, dim.width, dim.height)) {   //Frustum Culling.
            //If it has a texture, it's a static asset.
            //prefer animation component
            if (mAnimation.has(entity)) {
                Animation animation = mAnimation.create(entity);
                boolean flip = animation.direction == Animation.Direction.LEFT;

                batch.draw(resourceManager.getAnimationKeyFrame(animation.animationName, animation.stateTime, animation.looping),
                        flip ? pos.x + dim.width : pos.x,
                        pos.y,
                        flip ? -dim.width : dim.width,
                        dim.height);
            } else if (mTexture.has(entity)) {
                batch.draw(resourceManager.getStaticAsset(mTexture.create(entity).resourceName),
                        pos.x,
                        pos.y,
                        dim.width,
                        dim.height);
            } else {
                logger.error("Missing texture information for {}", entity);
                //Fallback case.
                batch.draw(resourceManager.getStaticAsset("null"),
                        pos.x,
                        pos.y,
                        dim.width,
                        dim.height
                );
            }
        }
    }

    private void renderBoundingBox(int entity) {
        shapeRenderer.setColor(255f, 0, 0, 0);
        if (mCollidable.has(entity)) {
            Collidable coll = mCollidable.create(entity);
            Position position = mPosition.create(entity);
            shapeRenderer.rect(position.x, position.y, coll.width, coll.height);
        }
    }

    private class ZComparator implements Comparator<Integer> {
        private World world;
        private float isometricRangePerYValue = 100f;

        ZComparator(World world, float isometricRangePerYValue) {
            this.world = world;
            this.isometricRangePerYValue = isometricRangePerYValue;
        }

        @Override
        public int compare(Integer e1, Integer e2) {

            //TODO Need to take into account the offset variable in layer.
            return (int) Math.signum(
                    (-world.getMapper(Position.class).create(e1).y * isometricRangePerYValue)
                            + (world.getMapper(Position.class).create(e2).y * isometricRangePerYValue));
        }
    }
}
