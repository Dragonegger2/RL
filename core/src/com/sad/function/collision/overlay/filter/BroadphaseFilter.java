package com.sad.function.collision.overlay.filter;

import com.sad.function.collision.overlay.broadphase.IBroadphaseFilter;
import com.sad.function.collision.overlay.Filter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;

public class BroadphaseFilter extends DefaultFilter<Body, BodyFixture> implements IBroadphaseFilter<Body, BodyFixture> {

    @Override
    public boolean isAllowed(Body body1, BodyFixture bodyFixture1, Body body2, BodyFixture bodyFixture2) {
        if(!body1.isActive() || !body2.isActive()) return false;
        if(!body1.isDynamic() && !body2.isDynamic() && !bodyFixture1.isSensor() && !bodyFixture2.isSensor()) return false;

        //TODO Island logic could go here.

        Filter filter1 = bodyFixture1.getFilter();
        Filter filter2 = bodyFixture2.getFilter();

        return filter1.isAllowed(filter2);
    }
}
