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


package com.google.gdata.data.projecthosting;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Project Hosting GData API.
 *
 * 
 */
public class ProjectHostingNamespace {

  private ProjectHostingNamespace() {}

  /** ProjectHostingIssues (ISSUES) namespace */
  public static final String ISSUES =
      "http://schemas.google.com/projecthosting/issues/2009";

  /** ProjectHostingIssues (ISSUES) namespace prefix */
  public static final String ISSUES_PREFIX = ISSUES + "#";

  /** ProjectHostingIssues (ISSUES) namespace alias */
  public static final String ISSUES_ALIAS = "issues";

  /** XML writer namespace for ProjectHostingIssues (ISSUES) */
  public static final XmlNamespace ISSUES_NS = new XmlNamespace(ISSUES_ALIAS,
      ISSUES);

  /** ProjectHostingProjects (PROJECTS) namespace */
  public static final String PROJECTS =
      "http://schemas.google.com/projecthosting/projects/2010";

  /** ProjectHostingProjects (PROJECTS) namespace prefix */
  public static final String PROJECTS_PREFIX = PROJECTS + "#";

  /** ProjectHostingProjects (PROJECTS) namespace alias */
  public static final String PROJECTS_ALIAS = "projects";

  /** XML writer namespace for ProjectHostingProjects (PROJECTS) */
  public static final XmlNamespace PROJECTS_NS = new
      XmlNamespace(PROJECTS_ALIAS, PROJECTS);

}

