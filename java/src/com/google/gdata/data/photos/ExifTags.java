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


package com.google.gdata.data.photos;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.photos.impl.ExifTag;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * A photo's exif tags. Exif tags are represented as a collection element with
 * nested elements, because that way clients can iterate over the exif tags
 * without having to know ahead of time exactly what is in it. We also support
 * retrieval of particular exif tags if the client knows what they want. Some
 * standard tags are supported with helper methods to retrieve them by name.
 *
 * 
 */
public class ExifTags extends ExtensionPoint implements Extensible, Extension {

  /*
   * Declare the extensions for our tags, which contains a single repeated tag
   * element that can have varying names (but a single exif namespace).
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(ExifTags.class, ExifTag.getDefaultDescription());
    extProfile.declareArbitraryXmlExtension(ExifTags.class);
    super.declareExtensions(extProfile);
  }

  /*
   * The default description for exif tags is just an element with the name
   * "tags"
   */
  public static ExtensionDescription getDefaultDescription() {
    return new ExtensionDescription(ExifTags.class, Namespaces.EXIF_NAMESPACE,
        "tags");
  }

  /**
   * Gets the exif tags as a collection of {@link ExifTag}.
   */
  public Collection<ExifTag> getExifTags() {
    return getRepeatingExtension(ExifTag.class);
  }

  /**
   * Get a particular exif tag by name.  This will retrieve the proper exif
   * tag based on the name.
   */
  public ExifTag getExifTag(String exifName) {
    Collection<ExifTag> tags = getExifTags();
    for (ExifTag tag : tags) {
      if (tag.getName().equals(exifName)) {
        return tag;
      }
    }
    return null;
  }

  /**
   * Gets the value of a particular exif tag, or null if it doesn't exist.
   */
  public String getExifTagValue(String exifName) {
    ExifTag tag = getExifTag(exifName);
    return tag == null ? null : tag.getValue();
  }

  /**
   * Convenience method to set an exif tag based on a float value.
   */
  public void setExifTagValue(String name, Number value) {
    if (value != null && value.floatValue() != 0.0F) {
      setExifTagValue(name, value.toString());
    } else {
      setExifTagValue(name, (String) null);
    }
  }
  
  /**
   * Sets the value of a particular exif tag by name.
   */
  public void setExifTagValue(String name, String value) {
    ExifTag tag = getExifTag(name);
    if (tag != null) {
      removeRepeatingExtension(tag);
    }
    if (value != null) {
      addRepeatingExtension(new ExifTag(name, value));
    }
  }

  // Convenience methods to retrieve well-known exif data.

  /**
   * @return the make of the camera, i.e. Nikon, Canon, Sony.
   */
  public String getCameraMake() {
    return getExifTagValue("make");
  }

  /**
   * Set the make of the camera used.
   */
  public void setCameraMake(String make) {
    setExifTagValue("make", make);
  }

  /**
   * @return the model of the camera used.
   */
  public String getCameraModel() {
    return getExifTagValue("model");
  }

  /**
   * Set the model of the camera used.
   */
  public void setCameraModel(String model) {
    setExifTagValue("model", model);
  }

  /**
   * @return the iso equivalent value used.
   * @throws ParseException if the value was not parsable as an integer.
   */
  public Integer getIsoEquivalent() throws ParseException {
    String iso = getExifTagValue("iso");
    if (iso == null) {
      return null;
    } else {
      try {
        return Integer.valueOf(iso);
      } catch (NumberFormatException nfe) {
        throw new ParseException("Invalid Iso field " + iso, nfe);
      }
    }
  }

  /**
   * Set the iso equivalent value used.
   */
  public void setIsoEquivalent(Integer iso) {
    setExifTagValue("iso", iso);
  }

  /**
   * @return the exposure time used.
   */
  public Float getExposureTime() throws ParseException {
    String exposure = getExifTagValue("exposure");
    if (exposure == null) {
      return null;
    } else {
      try {
        return Float.valueOf(exposure);
      } catch (NumberFormatException nfe) {
        throw new ParseException("Invalid exposure field " + exposure, nfe);
      }
    }
  }

  /**
   * Set the exposure time used.
   */
  public void setExposureTime(Float exposure) {
    setExifTagValue("exposure", exposure);
  }

  /**
   * @return the fstop value used.
   * @throws ParseException if the value is not a valid floating point number.
   */
  public Float getApetureFNumber() throws ParseException {
    String fstop = getExifTagValue("fstop");
    if (fstop == null) {
      return null;
    } else {
      try {
        return Float.valueOf(fstop);
      } catch (NumberFormatException nfe) {
        throw new ParseException("Invalid fstop field " + fstop, nfe);
      }
    }
  }

  /**
   * Set the fstop value used.
   */
  public void setApetureFNumber(Float fstop) {
    setExifTagValue("fstop", fstop);
  }

  /**
   * @return the distance to the subject.
   * @throws ParseException if the value is not a valid floating point number.
   */
  public Float getDistance() throws ParseException {
    String distance = getExifTagValue("distance");
    if (distance == null) {
      return null;
    } else {
      try {
        return Float.valueOf(distance);
      } catch (NumberFormatException nfe) {
        throw new ParseException("Invalid distance field " + distance, nfe);
      }
    }
  }

  /**
   * Set the distance to the subject.
   */
  public void setDistance(Float distance) {
    setExifTagValue("distance", distance);
  }

  /**
   * @return the time the photo was taken.
   * @throws ParseException if the value is not a number, represented as a long.
   */
  public Date getTime() throws ParseException {
    String time = getExifTagValue("time");
    if (time == null) {
      return null;
    } else {
      try {
        return new Date(Long.parseLong(time));
      } catch (NumberFormatException nfe) {
        throw new ParseException("Invalid time field " + time, nfe);
      }
    }
  }

  /**
   * Set the date/time the photo was taken.
   */
  public void setTime(Date time) {
    setExifTagValue("time",
        time == null ? null : Long.toString(time.getTime()));
  }

  /**
   * @return the focal length used.
   * @throws ParseException if the value is not a valid floating point number.
   */
  public Float getFocalLength() throws ParseException {
    String focalLength = getExifTagValue("focallength");
    if (focalLength == null) {
      return null;
    } else {
      try {
        return Float.valueOf(focalLength);
      } catch (NumberFormatException nfe) {
        throw new ParseException("Invalid focal length " + focalLength, nfe);
      }
    }
  }

  /**
   * Set the focal length used.
   */
  public void setFocalLength(Float focalLength) {
    setExifTagValue("focallength", focalLength);
  }

  /**
   * @return {@link Boolean#TRUE} if the flash was used.
   */
  public Boolean getFlashUsed() {
    String flash = getExifTagValue("flash");
    return flash == null ? null : Boolean.valueOf(flash);
  }

  /**
   * Set whether the flash was used.
   */
  public void setFlashUsed(Boolean flash) {
    setExifTagValue("flash", flash == null ? null : flash.toString());
  }

  /**
   * @return the unique image id for the photo.
   */
  public String getImageUniqueID() {
    return getExifTagValue("imageUniqueID");
  }

  /**
   * Set the unique image id for the photo.
   */
  public void setImageUniqueID(String imageUniqueID) {
    setExifTagValue("imageUniqueID", imageUniqueID);
  }


  /*
   * Generate the xml for this element.  This is hacked to support including
   * arbitrary exif fields as nested elements.
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    w.startElement(Namespaces.EXIF_NAMESPACE, "tags", null, null);
    Collection<ExifTag> fields = getExifTags();
    for (ExifTag field : fields) {
      field.generate(w, extProfile);
    }
    w.endElement();
  }

  /*
   * Get a handler for parsing this element.
   */
  @Override
  public ElementHandler getHandler(final ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new XmlParser.ElementHandler() {
      @Override
      public XmlParser.ElementHandler getChildHandler(String namespace,
          String localName, Attributes attrs)
          throws ParseException, IOException {
        if (Namespaces.EXIF.equals(namespace)) {
          ExifTag field = new ExifTag(localName);
          addRepeatingExtension(field);
          return field.getHandler(extProfile, namespace, localName, attrs);
        }
        return getExtensionHandler(extProfile, ExifTags.class, namespace,
            localName, attrs);
      }
    };
  }
}
