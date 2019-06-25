package com.sad.function.collision.overlay.broadphase;

import com.sad.function.collision.overlay.Filter;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.broadphase.filters.BroadphaseFilterAdapter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.container.Fixture;
import com.sad.function.collision.overlay.filter.DetectBroadphaseFilter;

public class DefaultBroadphaseFilter<E extends Collidable<T>, T extends Fixture> extends BroadphaseFilterAdapter<E, T> implements BroadphaseFilter<E, T> {
    @Override
    public boolean isAllowed(E body1, T bodyFixture1, E body2, T bodyFixture2) {
        Filter filter1 = bodyFixture1.getFilter();
        Filter filter2 = bodyFixture2.getFilter();

        return filter1.isAllowed(filter2);
    }
}
