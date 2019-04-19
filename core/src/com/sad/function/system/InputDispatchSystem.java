package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sad.function.common.Observer;
import com.sad.function.components.InputHandler;
import com.sad.function.input.MappedInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputDispatchSystem extends IteratingSystem implements Observer<MappedInput> {
    private MappedInput mappedInput;

    private static final Logger logger = LogManager.getLogger(InputDispatchSystem.class);
    private final ComponentMapper<InputHandler> input = ComponentMapper.getFor(InputHandler.class);

    public InputDispatchSystem() {
        super(Family.one(InputHandler.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //Where we actually process all entities that want/need input.
//        input.
    }

    @Override
    public void onNotify(Entity entity, MappedInput event) {
        this.mappedInput = event;
    }
}
