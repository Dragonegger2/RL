package com.sad.function.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.data.Transform;
import com.sad.function.collision.shape.Rectangle;
import com.sad.function.components.PhysicsBody;
import com.sad.function.components.TransformComponent;
import com.sad.function.global.GameInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@All({PhysicsBody.class, TransformComponent.class})
public class CollisionBodyRenderingSystem extends IteratingSystem {
    private static final Logger logger = LogManager.getLogger(CollisionBodyRenderingSystem.class);

    private ComponentMapper<PhysicsBody> mPhysicsBody;
    private ComponentMapper<TransformComponent> mTransformComponent;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    public CollisionBodyRenderingSystem(OrthographicCamera camera, ShapeRenderer shapeRenderer) {
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void process(int entityId) {
        if(GameInfo.RENDER_HITBOX_OUTLINES) {
            Body body = mPhysicsBody.create(entityId).body;
            Transform transform = mTransformComponent.create(entityId).transform;
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


            for (int j = 0; j < body.getFixtureCount(); j++) {
                Fixture f = body.getFixtures().get(j);

                Vector2 shapeCenter = f.getShape().getCenter();
                Vector2 position = transform.getTransformed(shapeCenter);

                if (f.getShape() instanceof Rectangle) {

                    Rectangle r = (Rectangle) f.getShape();


                    shapeRenderer.setColor(body.getColor());
                    if (f.isSensor()) shapeRenderer.setColor(Color.LIME);
                    shapeRenderer.rect(position.x - r.getWidth() / 2, position.y - r.getHeight() / 2, r.getWidth(), r.getHeight());
                } else {
                    logger.warn("No matching renderer for {}", f.getShape().getClass());
                }

                shapeRenderer.setColor(Color.FIREBRICK);
                shapeRenderer.circle(position.x, position.y, 0.125f, 15);
            }

            shapeRenderer.end();
        }
    }
}
