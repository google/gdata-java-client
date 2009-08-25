/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.model;

import com.google.gdata.util.common.base.Objects;
import com.google.gdata.util.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataKey;
import com.google.gdata.model.QName;

import java.util.List;

/**
 * The Path class represents an immutable path to a model entity in the GData
 * DOM. A path can be absolute (the root {@link ElementMetadata} for the path is
 * specified at construction time or relative (the root element type is unknown
 * at construction time).
 * <p>
 * The {@link #toAbsolute(ElementMetadata)} method can be used to produce an
 * absolute path from a relative path by interpreting it relative to a root.
 * <p>
 * New paths can be constructed using the {@link #of(MetadataKey...)} or
 * {@link #to(ElementMetadata, MetadataKey...)} methods as well as using the
 * {@link #builder()} method to obtain a new {@link Builder} instance that can
 * be used for incremental path construction.
 *
 * 
 */
public class Path {

  /**
   * A simple relative path that selects the root of the path.
   */
  public static final Path ROOT = builder().build();

  /**
   * The Builder class provides a model for incrementally constructing new
   * {@link Path} relative or absolute instances.
   */
  public static class Builder {

    private ElementMetadata<?, ?> root;
    private List<MetadataKey<?>> steps = Lists.newArrayList();
    private boolean selectsAttribute;
    private ElementMetadata<?, ?> selectedElement;
    private AttributeMetadata<?> selectedAttribute;

    // Constructed using Path.builder()
    private Builder() {}

    /**
     * Specifies the root element type that any path steps should be interpreted
     * as being relative to.   This will replace any existing root element type
     * for the builder and any existing steps will be revalidated relative t
     * the new root element type.
     *
     * @param root root element type for path
     * @return this builder instance (for chaining)
     * @throws PathException if this path has been bound to a
     *     metadata instance and no key with the given step can be found.
     * @throws NullPointerException if root is null.
     */
    Builder fromRoot(ElementMetadata<?, ?> root) {
      this.selectedElement = this.root = Preconditions.checkNotNull(root);
      if (steps != null) {
        // Recompute steps relative to the new root
        List<MetadataKey<?>> prevSteps = Lists.newArrayList(steps);
        steps.clear();
        for (MetadataKey<?> step : prevSteps) {
          addStep(step);
        }
      }
      return this;
    }

    /**
     * Adds a new path step to the end of the path. If the path is absolute the
     * step will be validated against the element type currently selected by the
     * path. If the step parameter is an {@link ElementKey}, then the step will
     * be valid if there is a child element type with the same {@link QName}. If
     * the step parameter is an {@link AttributeKey}, then it will be valid if
     * there is a matching child attribute type with the same {@link QName}. If
     * invalid, a {@link PathException} will be thrown. No validation is
     * performed for relative paths.
     *
     * @param step metadata key for new path step
     * @throws PathException if this path has been bound to a
     *     metadata instance and no key with the given step can be found, or
     *     if the path is an attribute path.  Once a path has an attribute key
     *     no more steps may be added.
     */
    public Builder addStep(MetadataKey<?> step) {

      if (selectedElement != null) {
        if (step instanceof ElementKey) {
          if (!addIfElement(step.getId())) {
            throw new PathException("No child element matching key:" + step);
          }
        } else {
          if (!addIfAttribute(step.getId())) {
            throw new PathException("No child attribute matching key:" + step);
          }
        }
      } else {
        // Unconditionally add steps to a relative path
        addToStepList(step);
      }
      return this;
    }

    /**
     * Conditionally adds a new element path step. For absolute paths, it will
     * be added if the {@link QName} matches a valid child element type for the
     * current selected element. If the path is relative a new
     * {@link ElementKey} step corresponding to the name will be unconditionally
     * added.
     *
     * @param id qualified name of child element step to add
     * @return {@code true} if added successfully, {@code false} otherwise.
     * @throws PathException if this path is an attribute path.  Once a path has
     *     an attribute key no more steps may be added.
     */
    public boolean addIfElement(QName id) {
      ElementKey<?, ?> elemKey;
      if (selectedElement != null) {
        elemKey = selectedElement.findElement(id);
        if (elemKey == null) {
          return false;
        }
        selectedElement = selectedElement.bindElement(elemKey);
      } else {
        elemKey = ElementKey.of(id);
      }
      addToStepList(elemKey);
      return true;
    }

    /**
     * Conditionally adds a new attribute path step. For absolute paths, it will
     * be added if the {@link QName} matches a valid child attribute type for
     * the current selected element. If the path is relative a new
     * {@link ElementKey} step corresponding to the name will be unconditionally
     * added.
     *
     * @param id qualified name of child attribute step to add
     * @return {@code true} if added successfully, {@code false} otherwise.
     * @throws PathException if this path is an attribute path.  Once a path has
     *     an attribute key no more steps may be added.
     */
    public boolean addIfAttribute(QName id) {
      AttributeKey<?> attrKey;
      if (selectedElement != null) {
        attrKey = selectedElement.findAttribute(id);
        if (attrKey == null) {
          return false;
        }
        selectedAttribute = selectedElement.bindAttribute(attrKey);
      } else {
        attrKey = AttributeKey.of(id);
      }
      addToStepList(attrKey);
      return true;
    }

    /**
     * Adds a single step to the end of the steps list.
     *
     * @throws PathException if this path is an attribute path.  Once a path has
     *     an attribute key no more steps may be added.
     */
    private void addToStepList(MetadataKey<?> step) {
      if (selectsAttribute) {
        throw new PathException(
            "Cannot add to an attribute path: " +step.getId());
      }
      if (step instanceof AttributeKey) {
        selectsAttribute = true;
      }
      steps.add(step);
    }

    /**
     * Returns a new {@link Path} instance based upon the current state of the
     * builder.
     */
    public Path build() {
      // No exception thrown here because the path is validated as its built
      return new Path(this);
    }
  }

  /*
   * Returns a new {link Builder} instance for path construction.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Constructs a new relative {@link Path} that selects an element defined by
   * the set of path steps to the element.
   *
   * @param steps keys defining steps to the selected element
   * @return selection path to element
   */
  public static Path of(MetadataKey<?> ... steps) {
    Builder builder = new Builder();
    for (MetadataKey<?> step : steps) {
      builder.addStep(step);
    }
    return builder.build();
  }

  /**
   * Constructs a new absolute {@link Path} to an element type as defined by a
   * root type and the relative steps from it to the selected type.
   *
   * @param keys keys defining steps to the selected element
   * @return selection path to element
   * @throws PathException if this path has been bound to a
   *     metadata instance and no key with the given step can be found, or
   *     if the path is an attribute path.  Once a path has an attribute key
   *     no more steps may be added.
   * @throws NullPointerException if root is null.
   */
  public static Path to(ElementMetadata<?, ?> root, MetadataKey<?> ... keys) {
    Builder builder = new Builder().fromRoot(root);
    for (MetadataKey<?> key : keys) {
      builder.addStep(key);
    }
    return builder.build();
  }

  /** root element type of the path (if absolute) or null (if relative) */
  private final ElementMetadata<?, ?> root;
  /** path steps */
  private final List<MetadataKey<?>> steps;
  /** true if the path selects an attribute, false otherwise */
  private final boolean selectsAttribute;
  /** element selected by the path or null (if relative) */
  private final ElementMetadata<?, ?> selectedElement;
  /** attribute selected by the path or null (if relative or an element path */
  private final AttributeMetadata<?> selectedAttribute;

  /**
   * Constructs a new {@link Path} based upon the state of the provided
   * {@link Builder} instance.
   */
  private Path(Builder builder) {
    root = builder.root;
    steps = ImmutableList.copyOf(builder.steps);
    selectsAttribute = builder.selectsAttribute;
    if (root == null) {
      selectedElement = null;
      selectedAttribute = null;
    } else {
      selectedElement = builder.selectedElement;
      selectedAttribute = builder.selectedAttribute;
    }
  }

  /**
   * Returns {@code true} if the path selects an attribute.
   */
  public boolean selectsAttribute() {
    return selectsAttribute;
  }

  /**
   * Returns {@code true} if the path selects an element.
   */
  public boolean selectsElement() {
    return !selectsAttribute;
  }

  /**
   * Returns the element type currently selected by the path or {@code null}
   * if the path is relative.
   */
  public ElementMetadata<?, ?> getSelectedElement() {
    return selectedElement;
  }

  /**
   * Returns the attribute type currently selected by the path or {@code null}
   * if the path selects an element or is relative.
   */
  public AttributeMetadata<?> getSelectedAttribute() {
    return selectedAttribute;
  }

  /**
   * Returns the list of path steps.
   */
  public List<MetadataKey<?>> getSteps() {
    return steps;
  }

  /**
   * Returns {@code true} if path is relative
   */
  public boolean isRelative() {
    return root == null;
  }

  /**
   * Returns the attribute key at the end of the path.
   *
   * @throws IllegalStateException if this path is not to an attribute.
   */
  public AttributeKey<?> getSelectedAttributeKey() {
    Preconditions.checkState(selectsAttribute,
        "Must select an attribute key.");
    return (AttributeKey<?>) steps.get(steps.size() - 1);
  }

  /**
   * Returns the element key at the end of the path.
   *
   * @throws IllegalStateException if this path is not to an element.
   */
  public ElementKey<?, ?> getSelectedElementKey() {
    Preconditions.checkState(!steps.isEmpty(),
        "Must not be an empty path.");
    Preconditions.checkState(!selectsAttribute,
        "Must select an element key.");
    return (ElementKey<?, ?>) steps.get(steps.size() - 1);
  }

  /**
   * Returns the element key for the second-to-last key in the path.  If the
   * path is only a single step this method will return {@code null}.
   */
  public ElementKey<?, ?> getParentKey() {
    if (steps.size() > 1) {
      return (ElementKey<?, ?>) steps.get(steps.size() - 2);
    }
    return null;
  }

  /**
   * Constructs a new {@link Path} instance by interpreting the steps in the
   * current path relative to the provided root {@link ElementMetadata}.
   *
   * @param root root of returned path
   * @return new absolute {@link Path} bound to the root element metadata.
   * @throws PathException if the path is not found in the metadata.
   * @throws NullPointerException if root is null.
   */
  public Path toAbsolute(ElementMetadata<?, ?> root) {
    Builder builder = new Builder().fromRoot(root);
    for (MetadataKey<?> step : steps) {
      builder.addStep(step);
    }
    return builder.build();
  }

  /**
   * The {@link #toString()} implementation is overridden to return the XPath
   * string that represents the path.
   */
  @Override
  public String toString() {
    if (steps.isEmpty()) {
      return ".";
    }
    StringBuilder builder = new StringBuilder();
    for (MetadataKey<?> step : steps) {
      addPathSeparator(builder);
      if (step instanceof AttributeKey) {
        builder.append('@');
      }
      builder.append(step.getId());
    }
    return builder.toString();
  }

  /**
   * Conditionally adds a path separator character to the builder if it has any
   * accumulated path.
   */
  private void addPathSeparator(StringBuilder builder) {
    if (builder.length() != 0) {
      builder.append('/');
    }
  }

  /**
   * The equals method will return true if the target object is also a
   * {@link Path}, has the same root or is also relative, and has the same list
   * of path steps.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null || o.getClass() != Path.class) {
      return false;
    }
    Path path = (Path) o;
    return root == path.root && steps.equals(path.steps);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(root, steps);
  }
}

