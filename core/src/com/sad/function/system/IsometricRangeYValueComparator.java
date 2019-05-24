package com.sad.function.system;

import com.artemis.World;
import com.sad.function.components.Layer;
import com.sad.function.components.Position;

import java.util.Comparator;

class IsometricRangeYValueComparator implements Comparator<Integer> {
    private World world;
    private float isometricRangePerYValue = 100f;

    IsometricRangeYValueComparator(World world) {
        this.world = world;
    }

    IsometricRangeYValueComparator(World world, float isometricRangePerYValue) {
        this.world = world;
        this.isometricRangePerYValue = isometricRangePerYValue;
    }

    @Override
    public int compare(Integer e1, Integer e2) {

        //TODO Need to take into account the offset variable in layer.
        //TODO Need to change what we use to get position. Otherwise the physics bodies we use won't ever get sorted properly.

        return (int) Math.signum(
                (-(world.getMapper(Position.class).create(e1).y * isometricRangePerYValue - world.getMapper(Layer.class).create(e1).yLayerOffset))
                        + (world.getMapper(Position.class).create(e2).y * isometricRangePerYValue - world.getMapper(Layer.class).create(e1).yLayerOffset));
    }
}
