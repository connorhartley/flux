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
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class MetaQuery {

    public static MetaQuery.Builder builder() {
        return new MetaQuery.Builder();
    }

    public static MetaQuery copyOf(final @NonNull MetaHolder metaHolder) {
        return new MetaQuery.Builder().requireOf(metaHolder).build();
    }

    public static MetaQuery of(final @NonNull String label) {
        return new MetaQuery.Builder().requireLabel(label).build();
    }

    public static MetaQuery of(final @NonNull String label, final @NonNull String value) {
        return new MetaQuery.Builder().requireTag(label, value).build();
    }

    public static MetaQuery ofAny(final @NonNull MetaHolder metaHolder) {
        return new MetaQuery.Builder().of(metaHolder).build();
    }

    public static MetaQuery ofAny(final @NonNull String label) {
        return new MetaQuery.Builder().label(label).build();
    }

    public static MetaQuery ofAny(final @NonNull String label, final @NonNull String value) {
        return new MetaQuery.Builder().tag(label, value).build();
    }

    private final Map<String, String> tags = new HashMap<>();
    private final Map<String, String> requiredTags = new HashMap<>();

    private MetaQuery(final MetaQuery.Builder builder) {
        this.tags.putAll(builder.tags);
        this.requiredTags.putAll(builder.requiredTags);
    }

    public boolean test(final @NonNull MetaHolder metaHolder) {
        final Stream<Map.Entry<String, String>> metaStream = metaHolder.tags().entrySet().stream();

        return metaStream.anyMatch(item -> this.tags.containsKey(item.getKey()) && this.tags.get(item.getKey()).equals(item.getValue()))
                && metaStream.allMatch(item -> this.requiredTags.containsKey(item.getKey()) && this.requiredTags.get(item.getKey()).equals(item.getValue()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tags, this.requiredTags);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof MetaQuery)) return false;
        final MetaQuery that = (MetaQuery) other;
        return Objects.equals(this.tags, that.tags)
                && Objects.equals(this.requiredTags, that.requiredTags);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tags", this.tags)
                .add("requiredTags", this.requiredTags)
                .toString();
    }

    public static class Builder {

        private final Map<String, String> tags = new HashMap<>();
        private final Map<String, String> requiredTags = new HashMap<>();

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

        public @NonNull Builder requireOf(final @NonNull MetaHolder other) {
            this.requiredTags.putAll(other.tags());
            return this;
        }

        public @NonNull Builder requireLabel(final @NonNull String label) {
            this.requiredTags.put(label, "");
            return this;
        }

        public @NonNull Builder requireTag(final @NonNull String label, final @NonNull String value) {
            this.requiredTags.put(label, value);
            return this;
        }

        public @NonNull MetaQuery build() {
            return new MetaQuery(this);
        }

    }

}
