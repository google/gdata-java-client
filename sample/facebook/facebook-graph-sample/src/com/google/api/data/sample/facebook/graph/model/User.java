/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.facebook.graph.model;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * @author Yaniv Inbar
 */
public final class User {

  @Key
  public String id;

  @Key("first_name")
  public String firstName;

  @Key("last_name")
  public String lastName;

  @Key
  public String name;

  @Key
  public String link;

  @Key
  public String about;

  @Key
  public String birthday;

  @Key
  public String email;
  
  @Key
  public String website;

  @Key
  public String hometown;

  @Key
  public String gender;

  @Key("interested_in")
  public List<String> interestedIn;
  
  @Key("meeting_for")
  public String meetingFor;
  
  @Key("relationship_status")
  public String relationshipStatus;
  
  @Key
  public String religion;
  
  @Key
  public String political;
  
  @Key
  public Boolean verified;
  
  @Key("significant_other")
  public String significantOther;
  
  @Key("timezone")
  public Integer timezone;
}
