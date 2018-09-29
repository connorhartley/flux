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
package com.ichorpowered.flux.meta;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MetaContainer implements MetaHolder {

    public static MetaContainer.Builder builder() {
        return new MetaContainer.Builder();
    }

    public static MetaContainer copyOf(final @NonNull MetaHolder metaHolder) {
        return new MetaContainer.Builder().of(metaHolder).build();
    }

    public static MetaContainer of(final @NonNull String label) {
        return new MetaContainer.Builder().label(label).build();
    }

    public static MetaContainer of(final @NonNull String label, final @NonNull String value) {
        return new MetaContainer.Builder().tag(label, value).build();
    }

    private final Map<String, String> tags = new HashMap<>();

    private MetaContainer(final MetaContainer.Builder builder) {
        this.tags.putAll(builder.tags);
    }

    @Override
    public @NonNull MetaHolder addTag(final @NonNull String label, final @NonNull String value) {
        this.tags.put(label, value);
        return this;
    }

    @Override
    public @NonNull MetaHolder addLabel(final @NonNull String label) {
        this.tags.put(label, "");
        return this;
    }

    @Override
    public boolean contains(final @NonNull String label) {
        return this.tags.containsKey(label);
    }

    @Override
    public boolean contains(final @NonNull String label, final @NonNull String value) {
        return this.tags.containsKey(label) && this.tags.get(label).equals(value);
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
        return ImmutableMap.copyOf(this.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tags);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof MetaContainer)) return false;
        final MetaContainer that = (MetaContainer) other;
        return Objects.equals(this.tags, that.tags);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tags", this.tags)
                .toString();
    }

    public static class Builder {

        private final Map<String, String> tags = new HashMap<>();

        private Builder() {}

        public @NonNull Builder of(final @NonNull MetaHolder other) {
            this.tags.putAll(other.tags());
            return this;
        }

        public @NonNull Builder label(final @NonNull String label) {
            this.tags.put(label, "");
            return this;
        }

        public @NonNull Builder tag(final @NonNull String label, final @NonNull String value) {
            this.tags.put(label, value);
            return this;
        }

        public @NonNull MetaContainer build() {
            return new MetaContainer(this);
        }

    }

}
