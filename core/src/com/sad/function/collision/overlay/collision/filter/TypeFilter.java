package com.sad.function.collision.overlay.collision.filter;


import com.sad.function.collision.overlay.collision.Filter;

public class TypeFilter implements Filter {
    @Override
    public boolean isAllowed(Filter filter) {
        if (filter == null) return false;
        if (this == filter) return true;
        if (filter instanceof TypeFilter) {
            return this.getClass().isInstance(filter) || filter.getClass().isInstance(this);
        }
        return false;
    }
}
