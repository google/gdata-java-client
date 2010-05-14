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

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Query;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The AppsForYourDomainService class extends the basic {@link GoogleService}
 * abstraction to define a service that is preconfigured for access to the
 * Google Apps for Your Domain GData API.
 */
public abstract class AppsForYourDomainService extends GoogleService {

  /**
   * The domain which hosts the the authentication.
   */
  public static final String DOMAIN_NAME = "www.google.com";

  /**
   * The protocol used to obtain authentication tokens.
   */
  public static final String HTTPS_PROTOCOL = "https";


  /**
   * The abbreviated name of Apps for Your Domain recognized by Google.
   * The service name is used while requesting an authentication token.
   */
  public static final String APPS_SERVICE = "apps";

  /**
   * Constructs a AppsForYourDomainService instance for an application with
   * the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public AppsForYourDomainService(String applicationName) {
    this(applicationName, HTTPS_PROTOCOL, DOMAIN_NAME);
  }

  /**
   * Constructs a GoogleService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}.  The service will authenticate at the provided
   * {@code domainName}.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *                        ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
  public AppsForYourDomainService(String applicationName,
      String protocol,
      String domainName) {
    super(APPS_SERVICE, applicationName, protocol, domainName);
  }

  /**
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   * occurred.
   */
  @Override
  public <E extends IEntry> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException, AppsForYourDomainException {
    try {
      return super.insert(feedUrl, entry);
    } catch (ServiceException se) {
      AppsForYourDomainException ae = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * The call to super.getEntry should fall through to Service and the
   * behaviour is unknown if this method is overloaded in GoogleService.
   *
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   * occurred.
   */
  @Override
  public <E extends IEntry> E getEntry(URL entryUrl,Class<E> entryClass)
      throws IOException, ServiceException, AppsForYourDomainException {
    try {
      return super.getEntry(entryUrl, entryClass);
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * The call to super.getEntry should fall through to Service and the
   * behaviour is unknown if this method is overloaded in GoogleService.
   *
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   * occurred.
   */
  @Override
  public <F extends IFeed> F getFeed(URL feedUrl, Class<F> feedClass)
      throws IOException, ServiceException, AppsForYourDomainException {
    try {
      return super.getFeed(feedUrl, feedClass);
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * The call to super.getEntry should fall through to Service and the
   * behaviour is unknown if this method is overloaded in GoogleService.
   *
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   * occurred.
   */
  @Override
  public <F extends IFeed> F query(Query query, Class<F> feedClass)
      throws IOException, ServiceException, AppsForYourDomainException {
    try {
      return super.query(query, feedClass);
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   * occurred.
   */
  @Override
  public <E extends IEntry> E update(URL entryUrl, E entry)
      throws IOException, ServiceException, AppsForYourDomainException {
    try {
      return super.update(entryUrl, entry);
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   * occurred.
   */
  @Override
  public void delete(URL entryUrl) throws IOException, ServiceException,
      AppsForYourDomainException {
    try {
      super.delete(entryUrl);
      return;
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * Returns an Atom entry instance, given the URL of the entry and an
   * if-modified-since date.  Note that this method is overriden to prevent the
   * usage of a non-null if-modified-since value.  The Google Apps for Your
   * Domain Provisioning API does not support the use of the if-modified-since
   * value. 
   *
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          entry should be returned only if it has been modified after the
   *          specified date. A value of {@code null} indicates no
   *          precondition.
   * @return the entry referenced by the URL parameter.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.NotModifiedException if the entry resource 
   *          has not been modified after the specified precondition date.
   * @throws com.google.gdata.util.ParseException error parsing the returned 
   *         entry.
   * @throws com.google.gdata.util.ResourceNotFoundException if the entry URL 
   *         is not valid.
   * @throws com.google.gdata.util.ServiceForbiddenException if the GData 
   *         service cannot get the entry resource due to access constraints.
   * @throws ServiceException if a system error occurred when retrieving
   *          the entry.
   */
  @Override
  public <E extends IEntry> E getEntry(URL entryUrl,
                                       Class<E> entryClass,
                                       DateTime ifModifiedSince)
      throws IOException, ServiceException {
    try {
      return super.getEntry(entryUrl, entryClass);
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * Executes a GData query against the target service and returns the
   * {@link IFeed} containing entries that match the query result, if
   * it's been modified since the specified date.  Note that this method is
   * overridden to prevent the usage of a non-null if-modified-since value.  The
   * Google Apps for Your Domain Provisioning API does not support the use of
   * the if-modified-since value.
   *
   * @param feedClass the Class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          query result feed should be returned only if contains entries
   *          that have been modified after the specified date.  A value of
   *          {@code null} indicates no precondition.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.NotModifiedException if the query resource 
   *         does not contain entries modified since the specified precondition
   *         date.
   * @throws com.google.gdata.util.ServiceForbiddenException feed does not 
   *         support the query.
   * @throws com.google.gdata.util.ParseException error parsing the returned 
   *         feed data.
   * @throws ServiceException query request failed.
   */
  @Override
  public <F extends IFeed> F getFeed(URL feedUrl,
                                     Class<F> feedClass,
                                     DateTime ifModifiedSince)
      throws IOException, ServiceException {
    try {
      return super.getFeed(feedUrl, feedClass);
    } catch (ServiceException se) {
      AppsForYourDomainException ae
          = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
  }

  /**
   * Executes a GData query against the target service and returns a
   * List containing all the {@link IEntry} that match the query result. 
   * This method will page through the feed and return all matching records 
   * to the client. Should be only used by services that have paged resultsets.
   * <p>Please note that this method may take a very long time to execute for 
   * feeds with thousands of entries.</p>
   * 
   * @param feedUrl resource URL of the feed
   * @param feedClass class used to represent service entries 
   * @return List of {@link IEntry}
   * @throws IOException error communicating with the GData service
   * @throws com.google.gdata.util.ServiceForbiddenException feed does not 
   *         support the query.
   * @throws com.google.gdata.util.ParseException error parsing the returned 
   *         feed data.
   * @throws ServiceException query request failed.
   */
  @SuppressWarnings("unchecked")
  protected <E extends IEntry, F extends IFeed> List<E> getAllPages(
      URL feedUrl, Class<F> feedClass) 
      throws IOException, ServiceException {
    List<E> allEntries = new ArrayList<E>();
    try {
      do {
        IFeed feed = getFeed(feedUrl, feedClass);
        allEntries.addAll((Collection<E>) feed.getEntries());
        feedUrl = (feed.getNextLink() == null)
          ? null : new URL(feed.getNextLink().getHref());
      } while (feedUrl != null); 
    } catch (ServiceException se) {
      AppsForYourDomainException ae = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
    return allEntries;
  }
}
