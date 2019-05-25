package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.sad.function.components.*;
import com.sad.function.global.GameInfo;
import com.sad.function.manager.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RenderingSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(RenderingSystem.class);
    //    private HashMap<Layer.RENDERABLE_LAYER, Boolean> layerNeedsSorting; TODO Future improvement?
    private IsometricRangeYValueComparator isometricRangeYValueComparator;

//    private Box2DDebugRenderer box2DDebugRenderer;

    private ComponentMapper<Layer> mLayer;
    private ComponentMapper<Position> mPosition;
    private ComponentMapper<TextureComponent> mTexture;
    private ComponentMapper<Dimension> mDimension;
    private ComponentMapper<Animation> mAnimation;
    private ComponentMapper<PhysicsBody> mPhysicsBody;
    private ComponentMapper<Collidable> mCollidable;

    private com.badlogic.gdx.physics.box2d.World pWorld;

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

    public RenderingSystem(ResourceManager resourceManager, com.badlogic.gdx.physics.box2d.World pWorld, Camera camera) {
        super(Aspect.all(Layer.class).one(Position.class, PhysicsBody.class).one(TextureComponent.class, Animation.class));

        this.resourceManager = resourceManager;
        this.camera = camera;
        this.pWorld = pWorld;

        layerCollections = new HashMap<>();
        idCollection = new IntMap<>();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
//        box2DDebugRenderer = new Box2DDebugRenderer();

        for (Layer.RENDERABLE_LAYER layer : layers) {
            layerCollections.put(layer, new ArrayList<>());
        }
    }

    @Override
    public void begin() {
        if (isometricRangeYValueComparator == null) {
            isometricRangeYValueComparator = new IsometricRangeYValueComparator(getWorld(), 100);
        }
        for (Layer.RENDERABLE_LAYER layer : layerCollections.keySet()) {
            //Going to sort the same way for all of the collections.
            //TODO Sort by y / ratio - yOffset of each entity.
            layerCollections.get(layer).sort(isometricRangeYValueComparator);
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

        Gdx.gl.glClearColor(100f / 255f, 149f / 255f, 237f / 255f, 1);
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
            shapeRenderer.setColor(Color.FIREBRICK);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            //Render only the default layer, for now.
            for (Integer integer : layerCollections.get(Layer.RENDERABLE_LAYER.DEFAULT)) {
                renderOutline(integer);
            }

            shapeRenderer.end();

//            box2DDebugRenderer.render(pWorld, camera.combined);
        }

    }

    private void renderOutline(int entity) {
        Vector3 pos = getPosition(entity);
        Dimension dim = mDimension.create(entity);

        if (inView(camera, pos, dim.width, dim.height)) {
            shapeRenderer.rect(pos.x, pos.y, dim.width, dim.height);
        }
    }

    private void renderEntity(int entity) {
        Vector3 pos = getPosition(entity);
        Dimension dim = mDimension.create(entity);

        if (inView(camera, pos, dim.width, dim.height)) {   //Frustum Culling: Check if in view before bothering to render the entity.
            batch.draw(getEntityTexture(entity),
                    shouldFlip(entity) ? pos.x + dim.width : pos.x,
                    pos.y,
                    shouldFlip(entity) ? -dim.width : dim.width,
                    dim.height);
        }
    }

    /**
     * Animations are the only ones that will really flip.
     *
     * @param entity to check for flippage.
     * @return if the renderer should flip the TextureRegion.
     */
    private boolean shouldFlip(int entity) {
        if (mAnimation.has(entity)) {
            return mAnimation.create(entity).direction == Animation.Direction.LEFT;
        }

        return false;
    }

    /**
     * Figure out which type of entity we are and get the correct texture region.
     *
     * @param entity to find the animation for.
     * @return the texture region from the AnimationManager.
     */
    private TextureRegion getEntityTexture(int entity) {
        if (mAnimation.has(entity)) {    //Animation assets.
            Animation animation = mAnimation.create(entity);

            return resourceManager.getAnimationKeyFrame(animation.animationName, animation.stateTime, animation.looping);
        } else {    //Static assets.
            return resourceManager.getStaticAsset(mTexture.create(entity).resourceName);
        }
    }

    private boolean inView(Camera camera, Vector3 position, float width, float height) {
        return camera.frustum.pointInFrustum(position.x, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + width, position.y, position.z) ||
                camera.frustum.pointInFrustum(position.x + width, position.y + height, position.z) ||
                camera.frustum.pointInFrustum(position.x, position.y + height, position.z);
    }

    private Vector3 getPosition(int entity) {
        if (mPhysicsBody.has(entity)) {
            PhysicsBody pBody = mPhysicsBody.create(entity);
            Dimension dimension = mDimension.create(entity);

            float sWidth = dimension.width;
            float sHeight = dimension.height;

            float xOffset = dimension.renderOffset.x;
            float yOffset = dimension.renderOffset.y;

            float renderX = pBody.position.x - sWidth / 2 - xOffset;
            float renderY = pBody.position.y - sHeight / 2 - yOffset;

            return new Vector3(renderX, renderY, 0.0f);
        }

        return new Vector3(mPosition.create(entity).x, mPosition.create(entity).y, mPosition.create(entity).z);
    }

    @Override
    public void dispose() {
//        box2DDebugRenderer.dispose();
        batch.dispose();
    }
}
