package com.sad.function.collision.detection.broadphase;

import com.sad.function.collision.Body;
import com.sad.function.collision.Fixture;
import com.sad.function.collision.filter.Filter;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilterAdapter;

public class DefaultBroadphaseFilter extends BroadphaseFilterAdapter implements BroadphaseFilter {
    @Override
    public boolean isAllowed(Body body1, Fixture bodyFixture1, Body body2, Fixture bodyFixture2) {
        Filter filter1 = bodyFixture1.getFilter();
        Filter filter2 = bodyFixture2.getFilter();

        return filter1.isAllowed(filter2);
    }
}