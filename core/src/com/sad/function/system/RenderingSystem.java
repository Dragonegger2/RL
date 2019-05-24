package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.IntMap;
import com.sad.function.components.*;
import com.sad.function.global.GameInfo;
import com.sad.function.manager.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RenderingSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(RenderingSystem.class);
    //    private HashMap<Layer.RENDERABLE_LAYER, Boolean> layerNeedsSorting; TODO Future improvement?
    private ZComparator zComparator;

    private Box2DDebugRenderer box2DDebugRenderer;

    private ComponentMapper<Layer> mLayer;
    private ComponentMapper<Position> mPosition;
    private ComponentMapper<TextureComponent> mTexture;
    private ComponentMapper<Dimension> mDimension;
    private ComponentMapper<Animation> mAnimation;
    private ComponentMapper<PhysicsBody> mPhysicsBody;
    private ComponentMapper<Collidable> mCollidable;

    private com.badlogic.gdx.physics.box2d.World pWorld;

    private SpriteBatch batch;
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

    public RenderingSystem(ResourceManager resourceManager, com.badlogic.gdx.physics.box2d.World pWorld, Camera camera) {
        super(Aspect.all(Layer.class).one(Position.class, PhysicsBody.class).one(TextureComponent.class, Animation.class));

        this.resourceManager = resourceManager;
        this.camera = camera;
        this.pWorld = pWorld;

        layerCollections = new HashMap<>();
        idCollection = new IntMap<>();

        batch = new SpriteBatch();
        box2DDebugRenderer = new Box2DDebugRenderer();

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

        Gdx.gl.glClearColor(100f/255f, 149f/255f, 237f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (Layer.RENDERABLE_LAYER layer : layers) {
            for (Integer integer : layerCollections.get(layer)) {
                renderEntity(integer);
            }
        }

        batch.end();

        if (GameInfo.DEBUG) {
            box2DDebugRenderer.render(pWorld, camera.combined);
        }

    }

    private boolean inView(Camera camera, Vector3 position, float width, float height) {
        return camera.frustum.pointInFrustum(position.x, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + width, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + width, position.y + height, position.z) ||
                camera.frustum.pointInFrustum(position.x, position.y + height, position.z);
    }

    private Vector3 getPosition(int entity) {
        if(world.getMapper(PhysicsBody.class).has(entity)) {
            return new Vector3(mPhysicsBody.create(entity).getPositionX(), mPhysicsBody.create(entity).getPositionY(), 0f);
        }
        return new Vector3(mPosition.create(entity).x,mPosition.create(entity).y, mPosition.create(entity).z);
    }

    private void renderEntity(int entity) {
        Vector3 pos = getPosition(entity);
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

//    private void renderBoundingBox(int entity) {
//        shapeRenderer.setColor(255f, 0, 0, 0);
//        if (mCollidable.has(entity)) {
//            Collidable coll = mCollidable.create(entity);
//            Position position = mPosition.create(entity);
//            shapeRenderer.rect(position.x + coll.xOffset, position.y + coll.yOffset, coll.width, coll.height);
//        }
//    }

    private class ZComparator implements Comparator<Integer> {
        private World world;
        private float isometricRangePerYValue = 100f;

        ZComparator(World world) {
            this.world = world;
        }

        ZComparator(World world, float isometricRangePerYValue) {
            this.world = world;
            this.isometricRangePerYValue = isometricRangePerYValue;
        }

        @Override
        public int compare(Integer e1, Integer e2) {

            //TODO Need to take into account the offset variable in layer.
            //TODO Need to change what we use to get position. Otherwise the physics bodies we use won't ever get sorted properly.

            return (int) Math.signum(
                    (-world.getMapper(Position.class).create(e1).y * isometricRangePerYValue)
                            + (world.getMapper(Position.class).create(e2).y * isometricRangePerYValue));
        }
    }

    @Override
    public void dispose() {
        box2DDebugRenderer.dispose();
        batch.dispose();
    }
}
