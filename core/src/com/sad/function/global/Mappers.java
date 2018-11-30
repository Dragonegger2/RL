package com.sad.function.global;

import com.badlogic.ashley.core.ComponentMapper;
import com.sad.function.components.PositionComponent;
import com.sad.function.components.TextureComponent;

public class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);

}
