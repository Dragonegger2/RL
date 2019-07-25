package com.sad.function.collision.filter;

public interface Filter {
    Filter DEFAULT_FILTER = filter -> true;

    boolean isAllowed(Filter filter);
}
