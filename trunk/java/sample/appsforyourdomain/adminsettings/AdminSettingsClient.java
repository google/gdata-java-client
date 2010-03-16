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


package sample.appsforyourdomain.adminsettings;

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.client.appsforyourdomain.adminsettings.AdminSettingsConstants;
import com.google.gdata.client.appsforyourdomain.adminsettings.DomainSettingsService;
import com.google.gdata.client.appsforyourdomain.adminsettings.DomainVerificationService;
import com.google.gdata.client.appsforyourdomain.adminsettings.EmailManagementService;
import com.google.gdata.client.appsforyourdomain.adminsettings.EncodeUtil;
import com.google.gdata.client.appsforyourdomain.adminsettings.SingleSignOnService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample client for Admin Settings API. Consists of drivers to run these
 * services: {@link DomainSettingsService} {@link SingleSignOnService}
 * {@link DomainVerificationService} {@link EmailManagementService}
 * 
 * 
 * 
 */
public class AdminSettingsClient {

  private static final Logger LOGGER = Logger.getLogger(AdminSettingsClient.class.getName());

  /**
   * run() does a demo run and performs all operations possible with the client
   * 
   * @param adminEmail the email id of the administrator.
   * @param password the administrator password.
   * @param domainName the domain name to be configured.
   */
  public static void runAccountSettings(String adminEmail, String password, String domainName) {
    System.out.println("Intiating demo run");
    try {
      DomainSettingsService client =
          new DomainSettingsService(adminEmail, password, domainName, "test");

      System.out.println("Enter full path to logo file");
      Scanner in = new Scanner(System.in);
      String file = in.nextLine(); 
      
      client.setDomainLogo(EncodeUtil.encodeBinaryFile(file));
      String countryCode = client.getCountryCodeForDomain();
      LOGGER.log(Level.INFO, "Retrieved CountryCode: " + countryCode);

      String customerPIN = client.getCustomerPIN();
      LOGGER.log(Level.INFO, "Retrieved customerPIN: " + customerPIN);

      String defaultLanguage = client.getDefaultLanguage();
      LOGGER.log(Level.INFO, "Retrieved defaultLanguage: " + defaultLanguage);

      String domainCreationTime = client.getDomainCreationTime();
      LOGGER.log(Level.INFO, "Retrieved domainCreationTime: " + domainCreationTime);

      String domainEdition = client.getDomainEdition();
      LOGGER.log(Level.INFO, "Retrieved domainEdition: " + domainEdition);

      String domainSecondaryEmailAddress = client.getDomainSecondaryEmailAddress();
      LOGGER.log(Level.INFO, "Retrieved domainSecondaryEmailAddress: "
          + domainSecondaryEmailAddress);

      String domainVerificationStatus = client.getDomainVerificationStatus();
      LOGGER.log(Level.INFO, "Retrieved domainVerificationStatus: " + domainVerificationStatus);

      int maxUserCount = client.getMaxUserCount();
      LOGGER.log(Level.INFO, "Retrieved maxUserCount: " + maxUserCount);

      String organizationName = client.getOrganizationName();
      LOGGER.log(Level.INFO, "Retrieved organizationName: " + organizationName);

      String supportPin = client.getSupportPIN();
      LOGGER.log(Level.INFO, "Retrieved supportPin: " + supportPin);

      System.out.println("Enter a secondary email ID for your domain:");
      String secondaryEmail = in.nextLine();

      LOGGER.log(Level.INFO, "Changing secondary email address to admin email: " + secondaryEmail);

      client.setDomainSecondaryEmailAddress(secondaryEmail);
      LOGGER.log(Level.INFO, "Changed secondary email address to : "
          + client.getDomainSecondaryEmailAddress());

      LOGGER.log(Level.INFO, "Changing organization name to newOrg:");
      client.setOrganizationName("newOrg");
      LOGGER.log(Level.INFO, "Changed organization name: " + client.getOrganizationName());

      LOGGER.log(Level.INFO, "Changing default language to fr:");
      client.setDefaultLanguage("fr");
      LOGGER.log(Level.INFO, "Changed default language: " + client.getDefaultLanguage());

      // Restore original values
      client.setDefaultLanguage(defaultLanguage);
      client.setDomainSecondaryEmailAddress(domainSecondaryEmailAddress);
      client.setOrganizationName(organizationName);

    } catch (AuthenticationException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (ServiceException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (MalformedURLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * A sample run for SSO settings API
   * 
   * @param adminEmail the email id of the administrator.
   * @param password the administrator password.
   * @param domainName the domain name to be configured.
   */
  public static void runSsoSettings(String adminEmail, String password, String domainName) {
    try {

      SingleSignOnService client =
          new SingleSignOnService(adminEmail, password, domainName, "test");

      final String key =   Base64.encode(("-----BEGIN CERTIFICATE-----\n"  
          + "MIIEbDCCA9WgAwIBAgIBCTANBgkqhkiG9w0BAQUFADCBjDELMAkGA1UEBhMCVVMx\n" 
          + "ETAPBgNVBAgTCE5ldyBZb3JrMREwDwYDVQQHEwhOZXcgWW9yazEPMA0GA1UEChMG\n" 
          + "R29vZ2xlMSQwIgYDVQQDFBtUaW0gRGllcmtzIENBIFtubyBzZWN1cml0eV0xIDAe\n"
          + "BgkqhkiG9w0BCQEWEWRpZXJrc0Bnb29nbGUuY29tMB4XDTA0MDQxNDIwMzM1NFoX\n"
          + "DTA1MDQxNDIwMzM1NFowQTEPMA0GA1UEChMGR29vZ2xlMRcwFQYDVQQLEw5TaW5n\n"
          + "bGUgU2lnbi1vbjEVMBMGA1UEAxMMTG9naW4gU2VydmVyMIIBtzCCASwGByqGSM44\n"
          + "BAEwggEfAoGBAKGpKYcoXxcgewIuAdDxT8QzSNI9I7Lja/LoueR1z7A/l0UWqZHO\n"
          + "6J8SyudgXFVxfkQEeYGbidsew2RMxvMl6pWMfqr/22eCqr9GPVkT7GVGqjAVdHVu\n"
          + "qJOPKSW7fQV3c82aj5g2qgkpwc1fUep8Cn1+Nz4ApCttVCSJD5kPtDTPAhUAo9jk\n"
          + "HrC8TH0kMFARmNbG5pizRS8CgYEAiB/TJmxCStDDMhDwo0ccnWgNo4oOQlMSeN46\n"
          + "Gb5YyVejeFBZSGni958ZcaaPW0Dg4VpbGxsQTSuF8P1BVY03fqimMd+dbRWSGgNy\n"
          + "YpkpdWBe21FnsSrnIrWnv/3K/7HMB7Xn4rEbhSvJF14I5TDuRN3lIOnIOKK6I5O9\n"
          + "QYfIfYoDgYQAAoGABPbEsbJS59Gj9556j9eAnGeLur56b98AGO7OFvYSoo9XcjoS\n"
          + "uYiFNhxu8MLzhkEqA6bqUif0mpl/d/VAXc74mdxaeg3vGb5MUGzdcr/mk9+32KYx\n"
          + "aX1hxn3UEN6WuypWe1eKuRVUzI/OepC88ib60XZHnkW9ByqqYXWdyGxW/G2jggEN\n"
          + "MIIBCTAJBgNVHRMEAjAAMCEGCWCGSAGG+EIBDQQUFhJHb29nbGUgLSBEaWVya3Mg\n"
          + "Q0EwHQYDVR0OBBYEFISoKKfKZlD6QIGqX60VCQA07sGLMIG5BgNVHSMEgbEwga6A\n"
          + "FK517Zhw+C2LH0rAjGRdEx61PYsIoYGSpIGPMIGMMQswCQYDVQQGEwJVUzERMA8G\n"
          + "A1UECBMITmV3IFlvcmsxETAPBgNVBAcTCE5ldyBZb3JrMQ8wDQYDVQQKEwZHb29n\n"
          + "bGUxJDAiBgNVBAMUG1RpbSBEaWVya3MgQ0EgW25vIHNlY3VyaXR5XTEgMB4GCSqG\n"
          + "SIb3DQEJARYRZGllcmtzQGdvb2dsZS5jb22CAQAwDQYJKoZIhvcNAQEFBQADgYEA\n"
          + "qwjvp27Xq1lp2ZyVWrGj8A3vuwUhsA2xGHvw4FTk4bCPwuuErugP/pwNl2582KNR\n"
          + "bjl1Vnz6zXkW1T4855EFWOZZkhIrvLGTRIoyQODCoW/Zd+3e7CfTvPdmJJNaVpD7\n"
          + "1RMPC45yjolVq4JLTT9/y6/+/5Nnn7oELnXRgDiMAR0=\n"
          + "-----END CERTIFICATE-----").getBytes());

      LOGGER.log(Level.INFO, "Retrieving SSO Settings: \n"
          + client.getSsoSettings().getAllProperties());
      LOGGER.log(Level.INFO, "Retrieving SSO Key: \n"
          + client.getSsoSigningKey().getAllProperties());
      LOGGER.log(Level.INFO, "Updating SSO Key: \n"
          + client.updateSsoSigningKey(key).getAllProperties());

    } catch (AuthenticationException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (ServiceException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (MalformedURLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * 
   * @param adminEmail the email id of the administrator.
   * @param adminPassword the administrator password.
   * @param domainName the domain name to be configured.
   */
  public static void runDomainVerificationService(String adminEmail, String password,
      String domainName) {
    try {
      DomainVerificationService client =
          new DomainVerificationService(adminEmail, password, domainName, "test");
      GenericEntry entry = client.retrieveCnameVerificationStatus();
      LOGGER.log(Level.INFO, "Retrieving CNAME verification status: " + entry.getAllProperties());

      entry = client.updateVerifiedStatus(entry, true);
      LOGGER.log(Level.INFO, "Updated CNAME verfication status: " + entry.getAllProperties());

      entry = client.retrieveMxVerificationStatus();
      LOGGER.log(Level.INFO, "Retrieving MX verification status: " + entry.getAllProperties());

      entry = client.updateVerifiedStatus(entry, true);
      LOGGER.log(Level.INFO, "Updating MX verfication status: " + entry.getAllProperties());

    } catch (AuthenticationException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (ServiceException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (MalformedURLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * 
   * @param adminEmail the email id of the administrator.
   * @param adminPassword the administrator password.
   * @param domainName the domain name to be configured.
   */
  public static void runEmailManagementService(
      String adminEmail, String password, String domainName) {
    try {
      EmailManagementService client =
          new EmailManagementService(adminEmail, password, domainName, "test");
      GenericEntry entry = client.retrieveOutboundGatewaySettings();
      LOGGER.log(Level.INFO, "Outbound gateway settings" + entry.getAllProperties());
      client.updateOutboundGatewaySettings(AdminSettingsConstants.TEST_EMAIL_ROUTE,
          AdminSettingsConstants.TEST_SMTPMODE);

      GenericFeed feed = client.retrieveEmailRoutingSettings();
      List<GenericEntry> entries = feed.getEntries();
      for (GenericEntry genericEntry : entries) {
        LOGGER.log(Level.INFO, "Email Routing Settings" + genericEntry.getAllProperties());
        LOGGER.log(Level.INFO, "Email Routing Settings" + genericEntry.getId());
        // update settings
        LOGGER.log(Level.INFO, "Updating Routing Settings");
        genericEntry.removeProperty("routeEnabled");
        genericEntry.addProperty("routeEnabled", String.valueOf(false));
        genericEntry.update();
      }

    } catch (AuthenticationException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (ServiceException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (MalformedURLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String adminEmail = parser.getValue("admin_email", "email", "e");
    String adminPassword = parser.getValue("admin_password", "pass", "p");
    String domain = parser.getValue("domain", "domain", "d");

    boolean help = parser.containsKey("help", "h");
    if (help || (adminEmail == null) || (adminPassword == null) || (domain == null)) {
      usage();
      System.exit(1);
    }

    System.out.println("WARNING: This sample modifies the admin settings of a domain."
        + "Use a TEST domain only.\nDo you want to continue (y/n):");
    Scanner in = new Scanner(System.in);
    String continueDemo = in.next();
    if (!String.valueOf(continueDemo).equalsIgnoreCase("y")) {
      System.exit(1);
    }
    LOGGER.log(Level.INFO, "-----------------Domain Settings Demo-------------");
    runAccountSettings(adminEmail, adminPassword, domain);

    LOGGER.log(Level.INFO, "-------------------SSO Settings Demo--------------");
    runSsoSettings(adminEmail, adminPassword, domain);

    LOGGER.log(Level.INFO, "--------------Verification Settings Demo----------");
    runDomainVerificationService(adminEmail, adminPassword, domain);

    LOGGER.log(Level.INFO, "---------------Email Management Demo--------------");
    runEmailManagementService(adminEmail, adminPassword, domain);
  }

  /*
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    System.out.println("Usage: java AdminSettngsClient"
        + " --admin_email [email] --admin_password [pass] --domain [domain]");
    System.out.println("\nA simple application that performs domain configuration \n"
        + "on the given domain using the provided admin username and password.\n"
        + "WARNING: Please use a test domain for the sample run.");
  }
}

