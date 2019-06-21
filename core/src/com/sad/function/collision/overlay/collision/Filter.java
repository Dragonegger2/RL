package com.sad.function.collision.overlay.collision;

public interface Filter {
    public static final Filter DEFAULT_FILTER = new Filter() {
        @Override
        public boolean isAllowed(Filter filter) {
            return true;
        }
    };

    boolean isAllowed(Filter filter);
}
