package com.sad.function.collision.overlay.listener;

import com.sad.function.collision.overlay.container.Body;
import com.sad.function.collision.overlay.container.BodyFixture;
import com.sad.function.collision.overlay.data.TimeOfImpact;

public class TimeOfImpactListenerAdapter implements TimeOfImpactListener {
    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, TimeOfImpact toi) {
        return false;
    }
}
