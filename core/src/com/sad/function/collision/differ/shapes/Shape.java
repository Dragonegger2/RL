package com.sad.function.collision.differ.shapes;

import com.badlogic.gdx.math.Vector2;
import com.sad.function.collision.differ.util.Matrix;
import com.sad.function.collision.differ.data.RayCollision;
import com.sad.function.collision.differ.data.ShapeCollision;

import java.util.HashMap;
import java.util.Map;

public abstract class Shape {
    public boolean active = true;
    public String name = "shape";
    public Object data;
    public Map<String, String> tags;
    public float x;
    public float y;

    protected Vector2 _position;
    protected float _rotation = 0;
    protected float _rotationRadians = 0;
    protected Vector2 _scale;

    protected float _scaleX;
    protected float _scaleY;

    protected boolean _transformed = false;
    protected Matrix _transformMatrix;

    public Shape(float x, float y) {
        tags = new HashMap<>();

        _position = new Vector2(x, y);
        _scale = new Vector2(1,1);
        _rotation = 0;
        _scaleX = 1;
        _scaleY = 1;

        _transformMatrix = new Matrix();
        _transformMatrix.makeTranslation(_position.x, _position.y);
    }

    public abstract ShapeCollision test(Shape shape, ShapeCollision into);
    public abstract ShapeCollision testCircle(Circle circle, ShapeCollision into, boolean flip); //Default is false;
    public abstract ShapeCollision testPolygon(Polygon polygon, ShapeCollision into, boolean flip);
    public abstract RayCollision testRay(Ray ray, RayCollision into);

    public void destroy() {
        _position = null;
        _scale = null;
        _transformMatrix = null;
    }

    public void refreshTransform() {
        _transformMatrix.compose(_position, _rotationRadians, _scale);
        _transformed = false;
    }

    public Vector2 getPosition() { return _position; }
    public Vector2 setPosiiton(Vector2 v) {
        _position = v;
        refreshTransform();
        return _position;
    }

    public float getX(){ return _position.x; }
    public float setX(float x) {
        _position.x = x;
        refreshTransform();
        return _position.x;
    }

    public float getY() { return _position.y; }
    public float setY(float y) {
        _position.y = y;
        refreshTransform();
        return _position.y;
    }

    public float getRotation() { return _rotation; }
    public float setRotation(float v) {
        _rotationRadians = v *(float)(Math.PI / 180);
        refreshTransform();
        return _rotation;
    }

    public float getScaleX() { return _scaleX; }
    public float setScaleX(float scale) {
        _scaleX = scale;
        _scale.y = _scaleX;
        refreshTransform();
        return _scaleX;
    }

    public float getScaleY() { return _scaleY; }
    public float setScaleY(float scale) {
        _scaleY = scale;
        _scale.y = _scaleY;
        refreshTransform();
        return _scaleY;
    }

    public Vector2 transform(Vector2 v, Matrix matrix) {
        Vector2 t = v.cpy();

        t.x = v.x*matrix.a + v.y*matrix.c + matrix.tx;
        t.y = v.x*matrix.b + v.y*matrix.d + matrix.ty;

        return t;
    }
}
