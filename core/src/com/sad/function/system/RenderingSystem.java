package com.sad.function.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
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

public class RenderingSystem extends BaseEntitySystem {
    private static final Logger logger = LogManager.getLogger(RenderingSystem.class);

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

    public RenderingSystem(ResourceManager resourceManager, Camera camera) {
        super(Aspect.all());

        this.resourceManager = resourceManager;
        this.camera = camera;
        shapeRenderer = new ShapeRenderer();

        entitiesPerLayer = new IntMap<>();
        layersToRender = new IntArray();

        batch = new SpriteBatch();
    }

    @Override
    public void begin() {
        if (dirty) {
            layersToRender.sort();
            dirty = false;
        }
    }

    @Override
    protected void inserted(int entity) {
        int zIndex = mLayer.create(entity).zIndex;
        IntSet entities = entitiesPerLayer.get(zIndex);
        if (entities == null) {
            entities = new IntSet();
            entitiesPerLayer.put(zIndex, entities);
            layersToRender.add(zIndex);
        }
        entities.add(entity);
        dirty = true;
    }

    @Override
    protected void removed(int entity) {
        int zIndex = mLayer.get(entity).zIndex;
        IntSet entities = entitiesPerLayer.get(zIndex);
        if (entities != null) {
            entities.remove(entity);
        }
    }

    @Override
    protected void processSystem() {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (int i = 0; i < layersToRender.size; i++) {
            IntSet.IntSetIterator iterator = entitiesPerLayer.get(layersToRender.get(i)).iterator();
            while (iterator.hasNext) {
                int entity = iterator.next();
                renderEntity(entity);

            }
        }

        batch.end();

        if (Global.DEBUG) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            for (int i = 0; i < layersToRender.size; i++) {
                IntSet.IntSetIterator iterator = entitiesPerLayer.get(layersToRender.get(i)).iterator();
                while (iterator.hasNext) {
                    int entity = iterator.next();
                    renderBoundingBox(entity);

                }
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
                logger.info("Missing texture information for {}", entity);
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
}
