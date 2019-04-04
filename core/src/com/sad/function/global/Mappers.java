package com.sad.function.global;

import com.badlogic.ashley.core.ComponentMapper;
import com.sad.function.components.Position;
import com.sad.function.components.Texture;

public class Mappers {
    public static final ComponentMapper<Position> position = ComponentMapper.getFor(Position.class);
    public static final ComponentMapper<Texture> texture = ComponentMapper.getFor(Texture.class);

}
