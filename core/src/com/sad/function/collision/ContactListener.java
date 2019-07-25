package com.sad.function.collision;

public interface ContactListener extends Listener {
    void persist(Contact contact);
    void begin(Contact contact);
    void end(Contact contact);
}
