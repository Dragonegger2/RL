package com.sad.function.collision.filter;

import com.sad.function.collision.detection.broadphase.DefaultFilter;
import com.sad.function.collision.detection.broadphase.filters.BroadphaseFilter;
import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;

public class DetectBroadphaseFilter extends DefaultFilter<Body, BodyFixture> implements BroadphaseFilter<Body, BodyFixture> {
    @Override
    public boolean isAllowed(Body body1, BodyFixture bodyFixture1, Body body2, BodyFixture bodyFixture2) {
        if(!body1.isActive() || !body2.isActive()) return false;
        if(!body1.isDynamic() && !body2.isDynamic() && !bodyFixture1.isSensor() && !bodyFixture2.isSensor()) return false;

        return super.isAllowed(body1, bodyFixture1, body2, bodyFixture2);
    }
}
