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

/**
 * Represents a type of {@link State} and the object
 * it contains of type {@code E} so it can be retrieved
 * from a container with the appropriate typing and
 * serialization.
 *
 * @param <E> the contained object type
 */
public class StateKey<E> {

    /**
     * Returns a new {@link StateKey.Builder}.
     *
     * @param <A> the contained object type
     * @return the new state key builder
     */
    public static <A> StateKey.Builder<A> builder() {
        return new StateKey.Builder<>();
    }

    private final String id;
    private final String name;
    private final TypeToken<E> elementType;
    private final E defaultElement;

    protected StateKey(final StateKey.Builder<E> builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.elementType = builder.elementType;
        this.defaultElement = builder.element;
    }

    /**
     * Returns the {@link State} identifier.
     *
     * @return the state identifier
     */
    public @NonNull String id() {
        return this.id;
    }

    /**
     * Returns the {@link State} name.
     *
     * @return the state name
     */
    public @NonNull String name() {
        return this.name;
    }

    /**
     * Returns the {@link TypeToken} element type.
     *
     * @return the state element type
     */
    public @NonNull TypeToken<E> elementType() {
        return this.elementType;
    }

    /**
     * Returns the default element of type {code E}.
     *
     * @return the default element
     */
    public @NonNull E defaultElement() {
        return this.defaultElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.elementType, this.defaultElement);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof StateKey<?>)) return false;
        final StateKey<?> that = (StateKey<?>) other;
        return Objects.equals(this.id, that.id())
                && Objects.equals(this.name, that.name())
                && Objects.equals(this.elementType, that.elementType())
                && Objects.equals(this.defaultElement, that.defaultElement());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("name", this.name)
                .add("elementType", this.elementType)
                .add("defaultElement", this.defaultElement)
                .toString();
    }

    /**
     * Represents a way to create {@link StateKey}s through
     * this builders provided options.
     *
     * @param <E> the contained object type
     */
    public static class Builder<E> {

        private String id;
        private String name;
        private TypeToken<E> elementType;
        private E element;

        protected Builder() {}

        /**
         * Returns this builder and sets the {@link State}
         * identifier for this new {@link StateKey} to be
         * created.
         *
         * @param id the identifier
         * @return this builder
         */
        public @NonNull Builder<E> id(final @NonNull String id) {
            this.id = id;
            return this;
        }

        /**
         * Returns this builder and sets the {@link State}
         * name for this new {@link StateKey} to be created.
         *
         * @param name the name
         * @return this builder
         */
        public @NonNull Builder<E> name(final @NonNull String name) {
            this.name = name;
            return this;
        }

        /**
         * Returns this builder and sets the {@link State}
         * default element and element {@link TypeToken} for
         * this new {@link StateKey} to be created.
         *
         * @param elementType the element type
         * @param defaultElement the default element
         * @return this builder
         */
        public @NonNull Builder<E> element(final @NonNull TypeToken<E> elementType, final @NonNull E defaultElement) {
            this.elementType = elementType;
            this.element = defaultElement;
            return this;
        }

        /**
         * Returns the new {@link StateKey} from the provided
         * options in this builder.
         *
         * @return the new state key
         */
        public @NonNull StateKey<E> build() {
            return new StateKey<>(this);
        }

    }

}
