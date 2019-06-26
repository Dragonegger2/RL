package com.sad.function.collision.overlay.collision.broadphase;

import com.sad.function.collision.overlay.filter.Filter;
import com.sad.function.collision.overlay.collision.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.collision.broadphase.filters.BroadphaseFilterAdapter;
import com.sad.function.collision.overlay.container.Fixture;

public class DefaultBroadphaseFilter<E extends Collidable<T>, T extends Fixture> extends BroadphaseFilterAdapter<E, T> implements BroadphaseFilter<E, T> {
    @Override
    public boolean isAllowed(E body1, T bodyFixture1, E body2, T bodyFixture2) {
        Filter filter1 = bodyFixture1.getFilter();
        Filter filter2 = bodyFixture2.getFilter();

        return filter1.isAllowed(filter2);
    }
}
