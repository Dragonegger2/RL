/*
 * Copyright (c) 2010-2016 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *     and the following disclaimer in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or
 *     promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.sad.function.collision.overlay.collision.filter;

import com.sad.function.collision.overlay.collision.Filter;

public class CategoryFilter implements Filter {
    /** The category this object is in */
    protected final long category;

    /** The categories this object can collide with */
    protected final long mask;

    /**
     * Default constructor.
     * <p>
     * By default the category is 1 and the mask is all categories.
     */
    public CategoryFilter() {
        this.category = 1;
        this.mask = Long.MAX_VALUE;
    }

    /**
     * Full constructor.
     * @param category the category bits
     * @param mask the mask bits
     */
    public CategoryFilter(long category, long mask) {
        super();
        this.category = category;
        this.mask = mask;
    }

    /**
     * Returns true if the given {@link Filter} and this {@link Filter}
     * allow the objects to interact.
     * <p>
     * If the given {@link Filter} is not the same type as this {@link Filter}
     * then a value of true is returned.
     * <p>
     * If the given {@link Filter} is null, a value of true is returned.
     * @param filter the other {@link Filter}
     * @return boolean
     */
    @Override
    public boolean isAllowed(Filter filter) {
        // make sure the given filter is not null
        if (filter == null) return true;
        // check the type
        if (filter instanceof CategoryFilter) {
            // cast the filter
            CategoryFilter cf = (CategoryFilter) filter;
            // perform the check
            return (this.category & cf.mask) > 0 && (cf.category & this.mask) > 0;
        }
        // if its not of right type always return true
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof CategoryFilter) {
            CategoryFilter filter = (CategoryFilter)obj;
            return filter.category == this.category && filter.mask == this.mask;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + (int)((this.category >>> 32) ^ this.category);
        hash = hash * 31 + (int)((this.mask >>> 32) ^ this.mask);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CategoryFilter[Category=").append(this.category)
                .append("|Mask=").append(this.mask)
                .append("]");
        return sb.toString();
    }

    /**
     * Returns the category bits.
     * @return long the category bits
     */
    public long getCategory() {
        return this.category;
    }

    /**
     * Returns the mask bits.
     * @return long the mask bits
     */
    public long getMask() {
        return this.mask;
    }
}
