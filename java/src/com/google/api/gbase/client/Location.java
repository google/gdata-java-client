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


package com.google.api.gbase.client;

/**
 * Object representation of a location; an address and
 * optionally latitude and longitude.
 *
 * 
 */
public class Location {
  private boolean hasCoordinates;
  private float latitude;
  private float longitude;
  private String address;

  /**
   * Creates an address location.
   *
   * @param address
   */
  public Location(String address) {
    setAddress(address);
  }

  /**
   * Creates a location with latitude, longitude and address.
   * @param address
   * @param latitude
   * @param longitude
   */
  public Location(String address, float latitude, float longitude) {
    setAddress(address);
    setLatitude(latitude);
    setLongitude(longitude);
  }

  /**
   * Check whether coordinates have been defined.
   *
   * @return true if coordinates have been defined
   */
  public boolean hasCoordinates() {
    return hasCoordinates;
  }

  /**
   * Unset latitude and longitude..
   */
  public void clearCoordinates() {
    this.hasCoordinates = false;
    this.latitude = 0.0f;
    this.longitude = 0.0f;
  }

  /**
   * Gets latitude, if it has been defined.
   *
   * @return latitude
   * @exception IllegalStateException if no coordinates have
   *   been defined (check with {@link #hasCoordinates()}.
   */
  public float getLatitude() {
    assertHasCoordinates();
    return latitude;
  }

  /**
   * Gets the longitude, if it has been defined.
   *
   * @return the longitude
   * @exception IllegalStateException if no coordinates have
   *   been defined (check with {@link #hasCoordinates()}.
   */
  public float getLongitude() {
    assertHasCoordinates();
    return longitude;
  }

  private void assertHasCoordinates() {
    if (!hasCoordinates) {
      throw new IllegalStateException("No coordinates have been defined. " +
          "(Check with hasCoordinates() first)");
    }
  }

  /**
   * Sets longitude.
   *
   * @param longitude
   */
  public void setLongitude(float longitude) {
    this.hasCoordinates = true;
    this.longitude = longitude;
  }

  /**
   * Sets latitude.
   *
   * @param latitude
   */
  public void setLatitude(float latitude) {
    this.hasCoordinates = true;
    this.latitude = latitude;
  }

  /**
   * Gets the address.
   *
   * @return address or null
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address.
   *
   * @param address an address
   * @exception NullPointerException if the
   *   address is null
   */
  public void setAddress(String address) {
    if (address == null) {
      throw new NullPointerException("address cannot be null");
    }
    this.address = address;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Location)) {
      return false;
    }

    final Location location = (Location) o;

    if (hasCoordinates != location.hasCoordinates) {
      return false;
    }
    if (hasCoordinates) {
      if (Float.compare(location.latitude, latitude) != 0) {
        return false;
      }
      if (Float.compare(location.longitude, longitude) != 0) {
        return false;
      }
    }
    return address.equals(location.address);
  }

  public int hashCode() {
    int result = address.hashCode();
    if (hasCoordinates) {
      result = 29 * result + Float.floatToIntBits(latitude);
      result = 29 * result + Float.floatToIntBits(longitude);
    }
    return result;
  }
}
