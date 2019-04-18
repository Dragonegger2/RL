package com.sad.function.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sad.function.components.Input;

import java.util.Comparator;

public class InputDispatchSystem extends IteratingSystem {
    private final ComponentMapper<Input> input = ComponentMapper.getFor(Input.class);

    public InputDispatchSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Input input =  this.input.get(entity);

        //Dispatch the input.
    }

    private static class PriorityComparator implements Comparator<Entity> {
        private ComponentMapper<Input> ic = ComponentMapper.getFor(Input.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)0;
        }
    }
}
