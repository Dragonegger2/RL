package com.sad.function.collision.differ.data;

public class RayCollisionHelper {
    /** Convenience: get the start X point along the line.
     It is possible the start point is not along the ray itself, when
     the `start` value is < 0, the ray start is inside the shape.
     If you need that point, use the ray.start point,
     i.e `if(data.start < 0) point = data.ray.start; else point = data.hitStart();  ` */
    public static float hitStartX(RayCollision data) {
        return data.ray.start.x + (data.ray.getDir().x * data.start);
    }

    /** Convenience: get the start Y point along the line */
    public static float hitStartY(RayCollision data) {
        return data.ray.start.y + (data.ray.getDir().y * data.start);
    }
    /**
         Convenience: get the end X point along the line.
         Note that it is possible that this extends beyond the length of the ray,
         when RayCollision `end` value is > 1, i.e the end of the ray is inside the shape area.
         If you need that point, you would use ray.end as the point,
         i.e `if(data.end > 1) point = data.ray.end; else point = data.hitEnd();`
     **/
    public static float hitEndX(RayCollision data) {
        return data.ray.start.x + (data.ray.getDir().x * data.end);
    }

    /** Convenience: get the end point along the line.
     Note that it is possible that this extends beyond the length of the ray,
     when RayCollision `end` value is > 1, i.e the end of the ray is inside the shape area.
     If you need that point, you would use ray.end as the point,
     i.e `if(data.end > 1) point = data.ray.end; else point = data.hitEnd();` */
    public static float hitEndY(RayCollision data) {
        return data.ray.start.y + (data.ray.getDir().y * data.end);
    }
}
