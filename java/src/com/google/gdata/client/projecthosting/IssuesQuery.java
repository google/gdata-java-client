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


package com.google.gdata.client.projecthosting;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the project issues feed.
 *
 * 
 */
public class IssuesQuery extends Query {

  /** Canned query identifier. */
  private String can;

  /** Issue ID. */
  private Integer id;

  /** Label. */
  private String label;

  /** Issue owner. */
  private String owner;

  /** Issue status. */
  private String status;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public IssuesQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the canned query identifier.
   *
   * @return canned query identifier or <code>null</code> to indicate that the
   *     parameter is not set.
   */
  public String getCan() {
    return can;
  }

  /**
   * Sets the canned query identifier.
   *
   * @param can canned query identifier or <code>null</code> to remove this
   *     parameter if set.
   */
  public void setCan(String can) {
    // check if setting to existing value
    if (this.can == null ? can != null : !this.can.equals(can)) {
      // set to new value for customer parameter
      this.can = can;
      setStringCustomParameter("can", can);
    }
  }

  /**
   * Returns the issue ID.
   *
   * @return issue ID or <code>null</code> to indicate that the parameter is not
   *     set.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the issue ID.
   *
   * @param id issue ID or <code>null</code> to remove this parameter if set.
   */
  public void setId(Integer id) {
    // check if setting to existing value
    if (this.id == null ? id != null : !this.id.equals(id)) {
      // set to new value for customer parameter
      this.id = id;
      setStringCustomParameter("id", id == null ? null : id.toString());
    }
  }

  /**
   * Returns the label.
   *
   * @return label or <code>null</code> to indicate that the parameter is not
   *     set.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label.
   *
   * @param label label or <code>null</code> to remove this parameter if set.
   */
  public void setLabel(String label) {
    // check if setting to existing value
    if (this.label == null ? label != null : !this.label.equals(label)) {
      // set to new value for customer parameter
      this.label = label;
      setStringCustomParameter("label", label);
    }
  }

  /**
   * Returns the issue owner.
   *
   * @return issue owner or <code>null</code> to indicate that the parameter is
   *     not set.
   */
  public String getOwner() {
    return owner;
  }

  /**
   * Sets the issue owner.
   *
   * @param owner issue owner or <code>null</code> to remove this parameter if
   *     set.
   */
  public void setOwner(String owner) {
    // check if setting to existing value
    if (this.owner == null ? owner != null : !this.owner.equals(owner)) {
      // set to new value for customer parameter
      this.owner = owner;
      setStringCustomParameter("owner", owner);
    }
  }

  /**
   * Returns the issue status.
   *
   * @return issue status or <code>null</code> to indicate that the parameter is
   *     not set.
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the issue status.
   *
   * @param status issue status or <code>null</code> to remove this parameter if
   *     set.
   */
  public void setStatus(String status) {
    // check if setting to existing value
    if (this.status == null ? status != null : !this.status.equals(status)) {
      // set to new value for customer parameter
      this.status = status;
      setStringCustomParameter("status", status);
    }
  }

}

