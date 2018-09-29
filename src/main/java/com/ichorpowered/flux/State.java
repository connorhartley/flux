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

public class State<E> implements ContainerHolder {

    public static <A> State.Builder<A> builder() {
        return new State.Builder<>();
    }

    public static <A> State<A> of(final @NonNull StateKey<A> key) {
        return new State.Builder<A>().key(key).build();
    }

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

    private State(final State.Builder<E> builder) {
        this.localTags.putAll(builder.tags);

        this.key = builder.key;
        this.element = builder.element;
        this.previousElement = builder.previousElement;

        this.updateTags();
    }

    public @NonNull StateKey<E> key() {
        return this.key;
    }

    public @NonNull E get() {
        return this.element == null ? this.key.element() : this.element;
    }

    public @NonNull Optional<E> getDirect() {
        return Optional.ofNullable(this.element);
    }

    public @NonNull E getDefault() {
        return this.key.element();
    }

    public @NonNull Optional<E> previous() {
        return Optional.ofNullable(this.previousElement);
    }

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

    public static class Builder<E> {

        private final Map<String, String> tags = new HashMap<>();

        private StateKey<E> key;
        private E element;
        private E previousElement;

        private Builder() {}

        public @NonNull Builder<E> of(final @NonNull MetaHolder other) {
            this.tags.putAll(other.tags());
            return this;
        }

        public @NonNull Builder<E> label(final @NonNull String label) {
            this.tags.put(label, "");
            return this;
        }

        public @NonNull Builder<E> tag(final @NonNull String label, final @NonNull String value) {
            this.tags.put(label, value);
            return this;
        }

        public @NonNull Builder<E> key(final @NonNull StateKey<E> key) {
            this.key = key;
            return this;
        }

        public @NonNull Builder<E> element(final @NonNull E element) {
            this.element = element;
            return this;
        }

        public @NonNull Builder<E> previousElement(final @NonNull E previousElement) {
            this.previousElement = previousElement;
            return this;
        }

        public @NonNull State<E> build() {
            return new State<>(this);
        }

    }

}
