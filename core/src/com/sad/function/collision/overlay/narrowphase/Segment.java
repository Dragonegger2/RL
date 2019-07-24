package com.sad.function.collision.overlay.narrowphase;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.dyn4j.Epsilon;

@SuppressWarnings("ALL")
public class Segment {
    public static Vector2 getPointOnSegmentClosestToPoint(Vector2 point, Vector2 linePoint1, Vector2 linePoint2) {
        Vector2 p1ToP = new Vector2(point.x - linePoint1.x, point.y - linePoint1.y);
        Vector2 line = linePoint2.cpy().sub(linePoint1);
        float ab2 = line.dot(line);
        float ap_ab = p1ToP.dot(line);

        if(ab2 <= Epsilon.E) return linePoint1.cpy();
        float t = ap_ab/ab2;
        t = MathUtils.clamp(t, 0.0f, 1.0f);
        return line.cpy().scl(t).add(linePoint1);
    }

}
