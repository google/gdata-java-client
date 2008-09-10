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


package com.google.gdata.client.appsforyourdomain;

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainService;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;

/**
 * The {@code AppsPropertyService} class extends the basic
 * {@link AppsForYourDomainService} abstraction to define a service that is
 * preconfigured for access to feeds using the {@code <apps:property>}
 * extension element, such as the Domain Configuration API.
 */
public class AppsPropertyService extends AppsForYourDomainService {

  /**
   * Constructs an {@code AppsPropertyService} instance for an application with
   * the name {@code applicationName}.
   * 
   * @param applicationName the name of the client application accessing the
   *        service. Application names should preferably have the format
   *        [company-id]-[app-name]-[app-version]. The name will be used by the
   *        Google servers to monitor the source of authentication.
   */
  public AppsPropertyService(String applicationName) {
    super(applicationName, HTTPS_PROTOCOL, DOMAIN_NAME);
    new GenericFeed().declareExtensions(getExtensionProfile());
  }

}
