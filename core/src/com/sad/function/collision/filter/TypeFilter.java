package com.sad.function.collision.filter;

public class TypeFilter implements Filter {
    @Override
    public boolean isAllowed(Filter filter) {
        if (filter == null) return false;
        if (this == filter) return true;
//        if (filter instanceof TypeFilter) {
//        if(GWTUtil.isInstanceOf(TypeFilter.class, filter)) {
//            return this.getClass().isInstance(filter) || filter.getClass().isInstance(this);
//        }
        return false;
    }
}
