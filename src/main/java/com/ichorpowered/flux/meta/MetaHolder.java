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

import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a meta holder that can be,
 * added and searched through using {@link String}
 * labels and values, otherwise known together
 * as tags.
 */
public interface MetaHolder {

    /**
     * Returns this {@link MetaHolder} and adds the
     * label and value pair to this {@link MetaHolder}.
     *
     * @param label the provided label
     * @param value the provided value
     * @return this meta holder
     */
    @NonNull MetaHolder addTag(@NonNull String label, @NonNull String value);

    /**
     * Returns this {@link MetaHolder} and adds the
     * label to this {@link MetaHolder}.
     *
     * @param label the provided label
     * @return this meta holder
     */
    @NonNull MetaHolder addLabel(@NonNull String label);

    /**
     * Returns {@code true} if this {@link MetaHolder}
     * contains the provided label, {@code false} if it
     * does not.
     *
     * @param label the provided label
     * @return true if the label is contained, otherwise false
     */
    boolean contains(@NonNull String label);

    /**
     * Returns {@code true} if this {@link MetaHolder}
     * contains the provided label and value pair, {@code false}
     * if it does not.
     *
     * @param label the provided label
     * @param value the provided value
     * @return true if the tag is contained, otherwise false
     */
    boolean contains(@NonNull String label, @NonNull String value);

    /**
     * Returns {@code true} if this {@link MetaHolder}
     * contains any of the tags provided by the other
     * {@code T} {@link MetaHolder}, {@code false} if
     * it does not.
     *
     * @param other the provided meta holder
     * @param <T> the provided meta holder type
     * @return true if any tags are contained, otherwise false
     */
    <T extends MetaHolder> boolean containsAny(@NonNull T other);

    /**
     * Returns {@code true} if this {@link MetaHolder}
     * contains all of the tags provided by the other
     * {@code T} {@link MetaHolder}, {@code false} if
     * it does not.
     *
     * @param other the provided meta holder
     * @param <T> the provided meta holder type
     * @return true if all tags are contained, otherwise false
     */
    <T extends MetaHolder> boolean containsAll(@NonNull T other);

    /**
     * Returns an {@link ImmutableMap} of all the label
     * and tag entries this {@link MetaHolder} contains
     * or inherits depending on the implementation.
     *
     * @return an immutable map of contained tags
     */
    @NonNull ImmutableMap<String, String> tags();

}
