/*
 * MIT License
 *
 * Copyright (c) 2017 Connor Hartley
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ichorpowered.flux;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public class StateKey<E> {

    public static <A> StateKey.Builder<A> builder() {
        return new StateKey.Builder<>();
    }

    private final String id;
    private final String name;
    private final TypeToken<E> elementType;
    private final E element;

    private StateKey(final StateKey.Builder<E> builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.elementType = builder.elementType;
        this.element = builder.element;
    }

    public @NonNull String id() {
        return this.id;
    }

    public @NonNull String name() {
        return this.name;
    }

    public @NonNull TypeToken<E> elementType() {
        return this.elementType;
    }

    public @NonNull E element() {
        return this.element;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.elementType, this.element);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof StateKey<?>)) return false;
        final StateKey<?> that = (StateKey<?>) other;
        return Objects.equals(this.id, that.id())
                && Objects.equals(this.name, that.name())
                && Objects.equals(this.elementType, that.elementType())
                && Objects.equals(this.element, that.element());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("name", this.name)
                .add("elementType", this.elementType)
                .add("element", this.element)
                .toString();
    }

    public static class Builder<E> {

        private String id;
        private String name;
        private TypeToken<E> elementType;
        private E element;

        private Builder() {}

        public @NonNull Builder<E> id(final @NonNull String id) {
            this.id = id;
            return this;
        }

        public @NonNull Builder<E> name(final @NonNull String name) {
            this.name = name;
            return this;
        }

        public @NonNull Builder<E> element(final @NonNull TypeToken<E> elementType, final @NonNull E defaultElement) {
            this.elementType = elementType;
            this.element = defaultElement;
            return this;
        }

        public @NonNull StateKey<E> build() {
            return new StateKey<>(this);
        }

    }

}
