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


package com.google.gdata.data.photos.impl;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Person;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.photos.GphotoData;
import com.google.gdata.data.photos.GphotoId;
import com.google.gdata.data.photos.GphotoNickname;
import com.google.gdata.data.photos.GphotoThumbnail;
import com.google.gdata.data.photos.GphotoType;
import com.google.gdata.data.photos.GphotoUsername;

import java.util.List;

/**
 * Basic implementation class for all of the {@link GphotoData} objects.  This
 * provides basic implementations of the standard methods that GphotoData
 * specifies as well as helper methods for subclasses to use when implementing
 * particular data interfaces.
 *
 * 
 */
public class GphotoDataImpl implements GphotoData {

  // The extension point we use for storing and retrieving data.
  private final ExtensionPoint extPoint;

  // The class of the extension point, cached to help declare extensions.
  private final Class<? extends ExtensionPoint> extClass;

  /**
   * Construct a new GphotoData implementation based on the given extension.
   */
  public GphotoDataImpl(ExtensionPoint extensionPoint) {
    this.extPoint = extensionPoint;
    this.extClass = extensionPoint.getClass();
  }

  /*
   * Declare the default gphoto:id, gphoto:type, and gphoto:rsslink extensions.
   */
  @SuppressWarnings("deprecation")
  public void declareExtensions(ExtensionProfile extProfile) {
    declare(extProfile, GphotoId.getDefaultDescription(false, false));
    declare(extProfile, GphotoType.getDefaultDescription(false, false));
    extProfile.declareArbitraryXmlExtension(extClass);

    // Declare that the person extension point can have user, nick, or thumb.
    extProfile.declare(Person.class,
        GphotoUsername.getDefaultDescription(false, false));
    extProfile.declare(Person.class,
        GphotoNickname.getDefaultDescription(false, false));
    extProfile.declare(Person.class,
        GphotoThumbnail.getDefaultDescription(false, false));
  }

  /**
   * Helper method to declare an extension as available only on the local entry
   * class.
   */
  protected void declare(ExtensionProfile extProfile,
      ExtensionDescription description) {
    extProfile.declare(extClass, description);

    // If we're declaring an entry class, we also need to declare that the
    // "BaseEntry" class has the same extensions, for auto-extension support.
    if (BaseEntry.class.isAssignableFrom(extClass)) {
      extProfile.declare(BaseEntry.class, description);
    }
  }

  /**
   * @return the Gphoto id.
   */
  public String getGphotoId() {
    return getSimpleValue(GphotoId.class);
  }

  /**
   * Sets the id of this entry.
   */
  public void setGphotoId(Long id) {
    if (id != null) {
      setExtension(GphotoId.from(id));
    } else {
      removeExtension(GphotoId.class);
    }
  }

  /**
   * Sets the id of this entry.
   */
  public void setGphotoId(String id) {
    if (id != null) {
      setExtension(new GphotoId(id));
    } else {
      removeExtension(GphotoId.class);
    }
  }

  /**
   * Protected helper to get the simple value from a construct extension.
   */
  protected String getSimpleValue(Class<? extends ValueConstruct> extClass) {
    ValueConstruct construct = getExtension(extClass);
    return construct == null ? null : construct.getValue();
  }

  /**
   * Get an extension by class.
   * @see ExtensionPoint#getExtension(Class)
   */
  protected <T extends Extension> T getExtension(Class<T> extClass) {
    return extPoint.getExtension(extClass);
  }

  /**
   * Get a repeating extension by class.
   * @see ExtensionPoint#getRepeatingExtension(Class)
   */
  protected <T extends Extension> List<T> getRepeatingExtension(
      Class<T> extClass) {
    return extPoint.getRepeatingExtension(extClass);
  }

  /**
   * Set an extension.
   * @see ExtensionPoint#setExtension(Extension)
   */
  public void setExtension(Extension extension) {
    extPoint.setExtension(extension);
  }

  /**
   * Add an extension.
   * @see ExtensionPoint#addExtension(Extension)
   */
  public void addExtension(Extension extension) {
    extPoint.addExtension(extension);
  }

  /**
   * Add a repeating extension.
   * @see ExtensionPoint#addRepeatingExtension(Extension)
   */
  public void addRepeatingExtension(Extension extension) {
    extPoint.addRepeatingExtension(extension);
  }

  /**
   * Remove an extension by class.
   * @see ExtensionPoint#removeExtension(Class)
   */
  public void removeExtension(Class<? extends Extension> extensionClass) {
    extPoint.removeExtension(extensionClass);
  }

  /**
   * Remove an extension.
   * @see ExtensionPoint#removeExtension(Extension)
   */
  public void removeExtension(Extension extension) {
    extPoint.removeExtension(extension);
  }

  /*
   * Remove a repeating extension from the delegate.
   */
  public void removeRepeatingExtension(Extension ext) {
    extPoint.removeRepeatingExtension(ext);
  }
}
