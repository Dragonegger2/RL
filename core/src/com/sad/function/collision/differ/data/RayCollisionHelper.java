package com.sad.function.collision.differ.data;

public class RayCollisionHelper {
    /** Convenience: get the distanceFromStartOfRay X point along the line.
     It is possible the distanceFromStartOfRay point is not along the ray itself, when
     the `distanceFromStartOfRay` value is < 0, the ray distanceFromStartOfRay is inside the shape.
     If you need that point, use the ray.distanceFromStartOfRay point,
     i.e `if(data.distanceFromStartOfRay < 0) point = data.ray.distanceFromStartOfRay; else point = data.hitStart();  ` */
    public static float hitStartX(RayCollision data) {
        return data.ray.start.x + (data.ray.getDir().x * data.distanceFromStartOfRay);
    }

    /** Convenience: get the distanceFromStartOfRay Y point along the line */
    public static float hitStartY(RayCollision data) {
        return data.ray.start.y + (data.ray.getDir().y * data.distanceFromStartOfRay);
    }
    /**
         Convenience: get the distanceFromEndOfRay X point along the line.
         Note that it is possible that this extends beyond the length of the ray,
         when RayCollision `distanceFromEndOfRay` value is > 1, i.e the distanceFromEndOfRay of the ray is inside the shape area.
         If you need that point, you would use ray.distanceFromEndOfRay as the point,
         i.e `if(data.distanceFromEndOfRay > 1) point = data.ray.distanceFromEndOfRay; else point = data.hitEnd();`
     **/
    public static float hitEndX(RayCollision data) {
        return data.ray.start.x + (data.ray.getDir().x * data.distanceFromEndOfRay);
    }

    /** Convenience: get the distanceFromEndOfRay point along the line.
     Note that it is possible that this extends beyond the length of the ray,
     when RayCollision `distanceFromEndOfRay` value is > 1, i.e the distanceFromEndOfRay of the ray is inside the shape area.
     If you need that point, you would use ray.distanceFromEndOfRay as the point,
     i.e `if(data.distanceFromEndOfRay > 1) point = data.ray.distanceFromEndOfRay; else point = data.hitEnd();` */
    public static float hitEndY(RayCollision data) {
        return data.ray.start.y + (data.ray.getDir().y * data.distanceFromEndOfRay);
    }
}
