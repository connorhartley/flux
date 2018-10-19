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
import com.ichorpowered.flux.ContainerHolder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a search for tags as a {@link MetaHolder}
 * for {@link ContainerHolder}s.
 */
public class MetaQuery {

    /**
     * Returns a new {@link MetaQuery.Builder}.
     *
     * @return the new meta query builder
     */
    public static MetaQuery.Builder builder() {
        return new MetaQuery.Builder();
    }

    /**
     * Returns a new {@link MetaQuery} with meta
     * tags from the provided {@link MetaHolder}
     * required to find from this query.
     *
     * @param metaHolder the provided meta holder
     * @return the new meta query
     */
    public static MetaQuery copyOf(final @NonNull MetaHolder metaHolder) {
        return new MetaQuery.Builder().requireOf(metaHolder).build();
    }

    /**
     * Returns a new {@link MetaQuery} with the
     * provided meta label required to find from
     * this query.
     *
     * @param label the provided meta label
     * @return the new meta query
     */
    public static MetaQuery of(final @NonNull String label) {
        return new MetaQuery.Builder().requireLabel(label).build();
    }

    /**
     * Returns a new {@link MetaQuery} with the
     * provided meta tag required to find from
     * this query.
     *
     * @param label the provided meta label
     * @param value the provided meta value
     * @return the new meta query
     */
    public static MetaQuery of(final @NonNull String label, final @NonNull String value) {
        return new MetaQuery.Builder().requireTag(label, value).build();
    }

    /**
     * Returns a new {@link MetaQuery} with meta
     * tags from the provided {@link MetaHolder}
     * optionally to find from this query.
     *
     * @param metaHolder the provided meta holder
     * @return the new meta query
     */
    public static MetaQuery copyOfAny(final @NonNull MetaHolder metaHolder) {
        return new MetaQuery.Builder().of(metaHolder).build();
    }

    /**
     * Returns a new {@link MetaQuery} with the
     * provided meta label optionally to find from
     * this query.
     *
     * @param label the provided meta label
     * @return the new meta query
     */
    public static MetaQuery ofAny(final @NonNull String label) {
        return new MetaQuery.Builder().label(label).build();
    }

    /**
     * Returns a new {@link MetaQuery} with the
     * provided meta tag required to find from
     * this query.
     *
     * @param label the provided meta label
     * @param value the provided meta value
     * @return the new meta query
     */
    public static MetaQuery ofAny(final @NonNull String label, final @NonNull String value) {
        return new MetaQuery.Builder().tag(label, value).build();
    }

    private final Map<String, String> tags = new HashMap<>();
    private final Map<String, String> requiredTags = new HashMap<>();

    protected MetaQuery(final MetaQuery.Builder builder) {
        this.tags.putAll(builder.tags);
        this.requiredTags.putAll(builder.requiredTags);
    }

    /**
     * Returns {@code true} if the provided
     * {@link MetaHolder} contains the tags this
     * {@link MetaQuery} is looking for.
     *
     * @param metaHolder the provided meta holder
     * @return true if the query accepts the tags, otherwise false
     */
    public boolean test(final @NonNull MetaHolder metaHolder) {
        final Stream<Map.Entry<String, String>> metaStream = metaHolder.tags().entrySet().stream();

        if (metaStream.allMatch(item -> this.requiredTags.containsKey(item.getKey()) && this.requiredTags.get(item.getKey()).equals(item.getValue()))) return true;
        return metaStream.anyMatch(item -> this.tags.containsKey(item.getKey()) && this.tags.get(item.getKey()).equals(item.getValue()));
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

    /**
     * Represents a way to create {@link MetaQuery}s
     * through this builders provided options.
     */
    public static class Builder {

        private final Map<String, String> tags = new HashMap<>();
        private final Map<String, String> requiredTags = new HashMap<>();

        protected Builder() {}

        /**
         * Returns this builder and collects the tags of
         * the other {@link MetaHolder} and copies them
         * over to the new {@link MetaQuery} this will
         * create.
         *
         * <p>This will add the tags to be optionally searched
         * not required to find from this query.</p>
         *
         * @param other the other meta holder
         * @return this builder
         */
        public @NonNull Builder of(final @NonNull MetaHolder other) {
            this.tags.putAll(other.tags());
            return this;
        }

        /**
         * Returns this builder and adds the label to the
         * new {@link MetaQuery} to be created.
         *
         * <p>This will add the tags to be optionally searched
         * not required to find from this query.</p>
         *
         * @param label the meta label
         * @return this builder
         */
        public @NonNull Builder label(final @NonNull String label) {
            this.tags.put(label, "");
            return this;
        }

        /**
         * Returns this builder and adds the tag to the
         * new {@link MetaQuery} to be created.
         *
         * <p>This will add the tags to be optionally searched
         * not required to find from this query.</p>
         *
         * @param label the meta label
         * @param value the meta value
         * @return this builder
         */
        public @NonNull Builder tag(final @NonNull String label, final @NonNull String value) {
            this.tags.put(label, value);
            return this;
        }

        /**
         * Returns this builder and collects the tags of
         * the other {@link MetaHolder} and copies them
         * over to the new {@link MetaQuery} this will
         * create.
         *
         * <p>This will add the tags to be searched
         * and required to find from this query.</p>
         *
         * @param other the other meta holder
         * @return this builder
         */
        public @NonNull Builder requireOf(final @NonNull MetaHolder other) {
            this.requiredTags.putAll(other.tags());
            return this;
        }

        /**
         * Returns this builder and adds the label to the
         * new {@link MetaQuery} to be created.
         *
         * <p>This will add the tags to be searched
         * and required to find from this query.</p>
         *
         * @param label the meta label
         * @return this builder
         */
        public @NonNull Builder requireLabel(final @NonNull String label) {
            this.requiredTags.put(label, "");
            return this;
        }

        /**
         * Returns this builder and adds the tag to the
         * new {@link MetaQuery} to be created.
         *
         * <p>This will add the tags to be searched
         * and required to find from this query.</p>
         *
         * @param label the meta label
         * @param value the meta value
         * @return this builder
         */
        public @NonNull Builder requireTag(final @NonNull String label, final @NonNull String value) {
            this.requiredTags.put(label, value);
            return this;
        }

        /**
         * Returns the new {@link MetaQuery} from the
         * provided options in this builder.
         *
         * @return the new meta query
         */
        public @NonNull MetaQuery build() {
            return new MetaQuery(this);
        }

    }

}
