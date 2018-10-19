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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ichorpowered.flux.meta.MetaHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an element {@code E} in a specific state. Whether
 * it contains an immutable or mutable object is up to the way
 * it is used, stored and accessed, there is no defined limitation,
 * but recommend immutability if possible.
 *
 * @param <E> the contained object type
 */
public class State<E> implements ContainerHolder {

    /**
     * Returns a new {@link State.Builder}.
     *
     * @param <A> the contained object type
     * @return the new state builder
     */
    public static <A> State.Builder<A> builder() {
        return new State.Builder<>();
    }

    /**
     * Returns a new {@link State} with meta updates from
     * the provided {@link MetaHolder}.
     *
     * @param metaHolder the provided meta holder
     * @param <A> the contained object type
     * @return the new state
     */
    public static <A> State<A> copyOf(final @NonNull MetaHolder metaHolder) {
        return new State.Builder<A>().of(metaHolder).build();
    }

    /**
     * Returns a new {@link State} with the provided key.
     *
     * @param key the provided key
     * @param <A> the contained object type
     * @return the new state
     */
    public static <A> State<A> of(final @NonNull StateKey<A> key) {
        return new State.Builder<A>().key(key).build();
    }

    /**
     * Returns a new {@link State} with the provided key
     * and element.
     *
     * @param key the provided key
     * @param element the provided element
     * @param <A> the contained object type
     * @return the new state
     */
    public static <A> State<A> of(final @NonNull StateKey<A> key, final @NonNull A element) {
        return new State.Builder<A>().key(key).element(element).build();
    }

    private ContainerHolder root;
    private ContainerHolder parent;

    private ImmutableMap<String, String> tags = ImmutableMap.of();

    private final Map<String, String> localTags = new HashMap<>();

    private final StateKey<E> key;
    private final E element;
    private final E previousElement;

    protected State(final State.Builder<E> builder) {
        this.localTags.putAll(builder.tags);

        this.key = builder.key;
        this.element = builder.element;
        this.previousElement = builder.previousElement;

        this.updateTags();
    }

    /**
     * Returns the associated key, with element type
     * and other identification for this {@link State}
     * and {@code E} element.
     *
     * @return the associated key for this state
     */
    public @NonNull StateKey<E> key() {
        return this.key;
    }

    /**
     * Returns the {@code E} element if present, otherwise
     * returns the default element inside the associated
     * {@link StateKey} for this {@link State}.
     *
     * @return the element if present, otherwise default element
     */
    public @NonNull E get() {
        return this.element == null ? this.key.defaultElement() : this.element;
    }

    /**
     * Returns the {@code E} element through an {@link Optional}
     * if it is present, otherwise returns empty.
     *
     * @return the element if present, otherwise empty
     */
    public @NonNull Optional<E> getDirect() {
        return Optional.ofNullable(this.element);
    }

    /**
     * Returns the {@code E} default element from the
     * associated {@link StateKey}.
     *
     * @return the default element
     */
    public @NonNull E getDefault() {
        return this.key.defaultElement();
    }

    /**
     * Returns the previous {@code E} element through an
     * {@link Optional} if it is preset, otherwise returns empty.
     *
     * @return the previous element if present, otherwise empty
     */
    public @NonNull Optional<E> previous() {
        return Optional.ofNullable(this.previousElement);
    }

    /**
     * Returns a new {@link State} with the provided {@code E} element,
     * and this {@link State} element as the previous element.
     *
     * @param element the provided element
     * @return the new state, with the provided element
     */
    public @NonNull State<E> set(final @NonNull E element) {
        return new Builder<E>().of(this)
                .key(this.key)
                .element(element)
                .previousElement(this.element)
                .build();
    }

    @Override
    public @NonNull <T extends ContainerHolder> Optional<T> root() {
        return Optional.ofNullable((T) this.root);
    }

    @Override
    public @NonNull <T extends ContainerHolder> Optional<T> parent() {
        return Optional.ofNullable((T) this.parent);
    }

    @Override
    public <T extends ContainerHolder> @Nullable T setParent(final @Nullable T parent) {
        this.parent = parent;
        this.root = null;

        this.updateTags();
        if (parent != null) this.root = parent.<T>root().orElse(null);

        return parent;
    }

    @Override
    public void updateTags() {
        this.tags = ImmutableMap.<String, String>builder()
                .putAll(this.parent == null ? Maps.newHashMap() : this.parent.tags())
                .putAll(this.localTags)
                .build();
    }

    @Override
    public @NonNull MetaHolder addTag(final @NonNull String label, final @NonNull String value) {
        this.localTags.put(label, value);
        this.updateTags();

        return this;
    }

    @Override
    public @NonNull MetaHolder addLabel(final @NonNull String label) {
        this.localTags.put(label, "");
        this.updateTags();

        return this;
    }

    @Override
    public boolean contains(final @NonNull String label) {
        return this.tags().containsKey(label);
    }

    @Override
    public boolean contains(final @NonNull String label, final @NonNull String value) {
        final ImmutableMap<String, String> requiredTags = this.tags();
        return requiredTags.containsKey(label) && requiredTags.get(label).equals(value);
    }

    @Override
    public <T extends MetaHolder> boolean containsAny(final @NonNull T other) {
        return other.tags().entrySet().stream()
                .anyMatch(test -> this.contains(test.getKey(), test.getValue()));
    }

    @Override
    public <T extends MetaHolder> boolean containsAll(final @NonNull T other) {
        return other.tags().entrySet().stream()
                .allMatch(test -> this.contains(test.getKey(), test.getValue()));
    }

    @Override
    public @NonNull ImmutableMap<String, String> tags() {
        return this.tags;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public int size() {
        return this.element == null ? 0 : 1;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Invalid attempt to clear an immutable state.");
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.root, this.parent, this.tags, this.key, this.element, this.previousElement);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof State<?>)) return false;
        final State<?> that = (State<?>) other;
        return Objects.equals(this.root, that.root)
                && Objects.equals(this.parent, that.parent)
                && Objects.equals(this.tags, that.tags)
                && Objects.equals(this.key, that.key)
                && Objects.equals(this.element, that.element)
                && Objects.equals(this.previousElement, that.previousElement);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("root", this.root)
                .add("parent", this.parent)
                .add("tags", this.tags)
                .add("key", this.key)
                .add("element", this.element)
                .add("previousElement", this.previousElement)
                .toString();
    }

    /**
     * Represents a way to create {@link State}s through
     * this builders provided options.
     *
     * @param <E> the contained object type
     */
    public static class Builder<E> {

        private final Map<String, String> tags = new HashMap<>();

        private StateKey<E> key;
        private E element;
        private E previousElement;

        protected Builder() {}

        /**
         * Returns this builder and collects the tags of
         * the other {@link MetaHolder} and copies them
         * over to the new {@link State} this will create.
         *
         * @param other the other meta holder
         * @return this builder
         */
        public @NonNull Builder<E> of(final @NonNull MetaHolder other) {
            this.tags.putAll(other.tags());
            return this;
        }

        /**
         * Returns this builder and adds the label to the
         * new {@link State} to be created.
         *
         * @param label the meta label
         * @return this builder
         */
        public @NonNull Builder<E> label(final @NonNull String label) {
            this.tags.put(label, "");
            return this;
        }

        /**
         * Returns this builder and adds the tag to the
         * new {@link State} to be created.
         *
         * @param label the meta label
         * @param value the meta value
         * @return this builder
         */
        public @NonNull Builder<E> tag(final @NonNull String label, final @NonNull String value) {
            this.tags.put(label, value);
            return this;
        }

        /**
         * Returns this builder and adds the key to the
         * new {@link State} to represent the contained
         * object this builder will create.
         *
         * @param key the state key
         * @return this builder
         */
        public @NonNull Builder<E> key(final @NonNull StateKey<E> key) {
            this.key = key;
            return this;
        }

        /**
         * Returns this builder and adds the element to the
         * new {@link State} to be created.
         *
         * @param element the state element
         * @return this builder
         */
        public @NonNull Builder<E> element(final @NonNull E element) {
            this.element = element;
            return this;
        }

        /**
         * Returns this builder and adds the previous element
         * to the new {@link State} to be created.
         *
         * @param previousElement the previous state element
         * @return this builder
         */
        public @NonNull Builder<E> previousElement(final @NonNull E previousElement) {
            this.previousElement = previousElement;
            return this;
        }

        /**
         * Returns the new {@link State} from the provided
         * options in this builder.
         *
         * @return the new state
         */
        public @NonNull State<E> build() {
            return new State<>(this);
        }

    }

}
