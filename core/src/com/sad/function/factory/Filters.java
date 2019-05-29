package com.sad.function.factory;

import com.badlogic.gdx.physics.box2d.Filter;
import com.sad.function.system.cd.EntityCategory;

import static com.sad.function.system.cd.EntityCategory.GROUND;
import static com.sad.function.system.cd.EntityCategory.PLAYER;

public class Filters extends Factory {

    //All 0xFFFF & ~ 0x0004
    public static final Filter playerFilter = playerFilter();
    private static Filter playerFilter() {
        Filter playerFilter = new Filter();

        playerFilter.categoryBits = (short)PLAYER.id;
        playerFilter.maskBits = (short)(0xFFFF & (~GROUND.id)); //Everybody but the ground.
        //~ not
        //| OR
        return playerFilter;
    }


    public static final Filter tileFilter = tileFilter();
    private static Filter tileFilter() {
        Filter tileFilter = new Filter();

        tileFilter.categoryBits = (short)(GROUND.id);
        return tileFilter;
    }

    public static class MaskBitBuilder {
        private int maskBits = -1;

        public short or(EntityCategory ... categories) {
            for (EntityCategory entityCategory : categories) {
                maskBits = entityCategory.id;
            }

            return (short)maskBits;
        }
    }
}