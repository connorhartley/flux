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

import com.ichorpowered.flux.meta.MetaHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

/**
 * Represents a container that can exist inside a
 * hierarchical structure where a parent and root
 * container exist and their tags are inherited to
 * the implementing container.
 */
public interface ContainerHolder extends MetaHolder {

    /**
     * Returns the root container in this containers
     * current hierarchy.
     *
     * @param <T> the container type
     * @return the container
     */
    @NonNull <T extends ContainerHolder> Optional<T> root();

    /**
     * Returns the parent container in this containers
     * current hierarchy.
     *
     * @param <T> the container type
     * @return the container
     */
    @NonNull <T extends ContainerHolder> Optional<T> parent();

    /**
     * Sets the parent container of this container in
     * the current hierarchy and returns it.
     *
     * @param parent the parent container
     * @param <T> the parent container type
     * @return the parent container
     */
    @Nullable <T extends ContainerHolder> T setParent(@Nullable T parent);

    /**
     * Clears this containers inherited tags and pulls
     * them again from its parent to update them.
     */
    void updateTags();

    /**
     * Returns {@code true} if this container, contains
     * other containers and {@code false} if it does not.
     *
     * @return true if this container has children
     */
    boolean hasChildren();

    /**
     * Returns the size of the elements and containers
     * contained inside this container.
     *
     * @return the container items size
     */
    int size();

    /**
     * Clears the items from this container.
     */
    void clear();

}
