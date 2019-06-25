package com.sad.function.collision.overlay;

public interface Filter {
    Filter DEFAULT_FILTER = filter -> true;

    boolean isAllowed(Filter filter);
}
