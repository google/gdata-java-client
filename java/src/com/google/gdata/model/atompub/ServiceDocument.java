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


package com.google.gdata.model.atompub;

import com.google.gdata.data.introspection.IServiceDocument;
import com.google.gdata.model.ContentModel;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.model.atom.TextContent;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Container for service information.
 *
 * 
 */
public class ServiceDocument extends Element implements IServiceDocument {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      ServiceDocument> KEY = ElementKey.of(new
      QName(Namespaces.atomPubStandardNs, "service"), Void.class,
      ServiceDocument.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.addElement(Workspace.KEY).setCardinality(
        ContentModel.Cardinality.MULTIPLE).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public ServiceDocument() {
    this(DefaultRegistry.get(KEY));
  }

  /**
   * Lets subclasses create an instance using custom metadata.
   */
  protected ServiceDocument(ElementMetadata<Void,
      ? extends ServiceDocument> metadata) {
    super(metadata);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param metadata metadata to use for this element.
   * @param source source element
   */
  public ServiceDocument(ElementMetadata<Void,
      ? extends ServiceDocument> metadata, Element source) {
    super(metadata, source);
  }

  /**
   * Returns the workspaces.
   *
   * @return workspaces
   */
  public List<Workspace> getWorkspaces() {
    return super.getElements(Workspace.KEY);
  }

  /**
   * Adds a new workspace.
   *
   * @param workspace workspace
   */
  public void addWorkspace(Workspace workspace) {
    super.addElement(Workspace.KEY, workspace);
  }

  /**
   * Removes an existing workspace.
   *
   * @param workspace workspace
   * @return true if the workspace was removed
   */
  public boolean removeWorkspace(Workspace workspace) {
    return super.removeElement(Workspace.KEY, workspace);
  }

  /**
   * Returns whether it has the workspaces.
   *
   * @return whether it has the workspaces
   */
  public boolean hasWorkspaces() {
    return super.hasElement(Workspace.KEY);
  }

  @Override
  public String toString() {
    return "{ServiceDocument}";
  }


  public Workspace addWorkspace(String title) {
    Workspace workspace = new Workspace(TextContent.plainText(title));
    addWorkspace(workspace);
    return workspace;
  }
}
