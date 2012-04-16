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


package sample.appsforyourdomain.gmailsettings;

import sample.util.SimpleCommandLineParser;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This is the command line client for the Google Apps Gmail Settings API.
 */
public class GmailSettingsClient {

  /**
   * Prevents the class from being instantiated.
   */
  private GmailSettingsClient() {
  }

  /**
   * Prints the command line usage of this sample application.
   */
  private static void printUsageAndExit() {
    System.out.println("Usage: java GmailSettingsClient"
        + " --username <username> --password <password> \n --domain <domain>\n"
        + " --setting <setting> [--disable]\n"
        + " [--get --destination_user <destination_user>] \n"
        + "[--delete --destination_user <destination_user> --delegationEmailId "
        + "<delegationEmailId>]");

    System.out.println();
    System.out.println("A simple application that demonstrates how to get, \n"
        + "change or delete Gmail settings in a Google Apps email account.\n"
        + "Authenticates using the provided admin login credentials, then retrieves\n"
        + "or modifies the settings of the specified account.");
    System.out.println();
    System.out.println("Specify username and destination_user as just the name,\n"
        + "not the email address.  For example, to change settings for\n"
        + "joe@example.com use these options:  --username joe --password\n"
        + "your_password --domain example.com");
    System.out.println();
    System.out.println("**To add/update settings...");
    System.out.println("Select which setting to change with the setting flag.\n"
        + "For example, to change the POP3 settings, use --setting pop\n"
        + "(allowed values are filter, sendas, label, forwarding, pop, imap,\n"
        + "vacation, signature, general, language, webclip, delegation.)");
    System.out.println();
    System.out.println("By default the selected setting will be enabled, \n"
        + "but with the --disable flag it will be disabled.");
    System.out.println();
    System.out.println("**To retrieve settings...");
    System.out.println("To retrieve settings, use the --get option and\n"
        + "mandatorily specify a single --destination_user.\n"
        + "For example, to get the signature settings, use\n"
        + "--get --settings signature --destination_user joe\n"
        + "(allowed values are label, sendas, forwarding, pop, imap, vacation,\n"
        + "signature, and delegation).");
    System.out.println();
    System.out.println("**To remove settings...");
    System.out.println("To remove settings, use the --delete option.\n"
        + "Deleting a setting is currently possible only for email delegation: use\n"
        + "--setting delegation. Supply --destination_user and --delegationEmailId\n");
    System.out.println();

    System.exit(1);
  }

  /**
   * Main entry point. Parses arguments and creates and invokes the
   * GmailSettingsClient
   *
   *  Usage: java GmailSettingsClient --username &lt;user&gt; --password
   * &lt;pass&gt; --domain &lt;domain&gt; --setting &lt;setting&gt; [--disable]
   * [--get true --destination_user &lt;destination_user&gt;] [--delete
   * --destination_user &lt;destination_user&gt; --delegationEmailId " +
   * "&lt;delegationEmailId&gt;]
   *
   * &lt;setting&gt; should be one of:
   * <ul>
   * <li>filter</li>
   * <li>sendas</li>
   * <li>label</li>
   * <li>forwarding</li>
   * <li>pop</li>
   * <li>imap</li>
   * <li>vacation</li>
   * <li>signature</li>
   * <li>general</li>
   * <li>language</li>
   * <li>webclip</li>
   * <li>delegation</li>
   * </ul>
   */
  public static void main(String[] arg) {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(arg);

    // Parse command-line flags
    String username = parser.getValue("username");
    String password = parser.getValue("password");
    String domain = parser.getValue("domain");
    String destinationUser = parser.getValue("destination_user");
    String delegationEmailId = parser.getValue("delegationEmailId");
    String setting = parser.getValue("setting");
    boolean doGet = parser.containsKey("get");
    boolean doDelete = parser.containsKey("delete");
    boolean doAddOrUpdate = false;

    boolean help = parser.containsKey("help");
    boolean enable = !parser.containsKey("disable");
    if (doGet && doDelete) {
      System.out.println(
          "Choose method as one of --get or --delete, or leave blank for create/update.\n");
      printUsageAndExit();
    } else if (!doGet && !doDelete) {
      doAddOrUpdate = true;
    }
    if (help || (username == null) || (password == null) || (domain == null) || (setting == null)
        || (doGet && destinationUser == null)
        || (setting.startsWith("delegation") && (doAddOrUpdate || doDelete)
            && (destinationUser == null || delegationEmailId == null))) {
      printUsageAndExit();
    }

    // --setting flag is quite accepting - case-insensitive and startsWith
    // mean that not just "pop" but also "POP3" works
    setting = setting.trim().toLowerCase();

    try {
      GmailSettingsService settings =
          new GmailSettingsService("exampleCo-exampleApp-1", domain, username, password);

      List<String> users = new ArrayList<String>();
      users.add(destinationUser);

      if (setting.startsWith("filter")) {
        if (doGet) {
          System.out.println("Retrieving filter settings is not supported.\n");
          printUsageAndExit();
        } else if (doDelete) {
          System.out.println("Deleting a filter is not supported.\n");
        } else {
          settings.createFilter(users,
              Defaults.FILTER_FROM,
              Defaults.FILTER_TO,
              Defaults.FILTER_SUBJECT,
              Defaults.FILTER_HAS_THE_WORD,
              Defaults.FILTER_DOES_NOT_HAVE_THE_WORD,
              Defaults.FILTER_HAS_ATTACHMENT,
              Defaults.FILTER_SHOULD_MARK_AS_READ,
              Defaults.FILTER_SHOULD_ARCHIVE,
              Defaults.FILTER_LABEL,
              Defaults.FILTER_FORWARD_TO,
              Defaults.FILTER_NEVER_SPAM,
              Defaults.FILTER_SHOULD_STAR,
              Defaults.FILTER_SHOULD_TRASH);
        }

      } else if (setting.startsWith("sendas")) {
        if (doGet) {
          List<Map<String, String>> sendAsSettings = settings.retrieveSendAs(destinationUser);
          if (sendAsSettings == null || sendAsSettings.size() == 0) {
            System.out.println("No send-as alias found.");
            return;
          }
          int count = 0;
          for (Map<String, String> sendAsSetting : sendAsSettings) {
            System.out.println("sendAs setting " + ++count + ":");
            Set<Entry<String, String>> entries = sendAsSetting.entrySet();
            for (Entry<String, String> entry : entries)
              System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
          }
        } else if (doDelete) {
          System.out.println("Removing a send-as alias is not supported.\n");
        } else {
          settings.createSendAs(users, Defaults.SEND_AS_NAME, Defaults.SEND_AS_ADDRESS,
              Defaults.SEND_AS_REPLY_TO, Defaults.SEND_AS_MAKE_DEFAULT);
        }

      } else if (setting.startsWith("label")) {
        if (doGet) {
          List<Map<String, String>> labels = settings.retrieveLabels(destinationUser);
          if (labels == null || labels.size() == 0) {
            System.out.println("No email labels found.");
            return;
          }
          int count = 0;
          for (Map<String, String> label : labels) {
            System.out.println("label " + ++count + ":");
            Set<Entry<String, String>> entries = label.entrySet();
            for (Entry<String, String> entry : entries)
              System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
          }
        } else if (doDelete) {
          System.out.println("Removing labels is not supported.\n");
        } else {
          settings.createLabel(users, Defaults.LABEL);
        }

      } else if (setting.startsWith("forwarding")) {
        if (doGet) {
          Map<String, String> forwarding = settings.retrieveForwarding(destinationUser);
          System.out.println("forwarding settings:");
          for (Entry<String, String> entry : forwarding.entrySet())
            System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
        } else if (doDelete) {
          System.out.println("Deleting forwarding settings is not possible. Consider "
              + "disabling forwarding by updating it.\n");
        } else {
          settings.changeForwarding(users, Defaults.FORWARDING_ENABLE,
              Defaults.FORWARDING_FORWARD_TO, Defaults.FORWARDING_ACTION);
        }

      } else if (setting.startsWith("pop")) {
        if (doGet) {
          Map<String, String> pop = settings.retrievePop(destinationUser);
          System.out.println("pop settings:");
          for (Entry<String, String> entry : pop.entrySet())
            System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
        } else if (doDelete) {
          System.out.println("Deleting POP settings is not possible. Consider "
              + "disabling POP by updating it.\n");
        } else {
          settings.changePop(
              users, Defaults.POP_ENABLE, Defaults.POP_ENABLE_FOR, Defaults.POP_ACTION);
        }

      } else if (setting.startsWith("imap")) {
        if (doGet) {
          boolean imap = settings.retrieveImap(destinationUser);
          System.out.println("imap settings:");
          System.out.println("\tenabled: " + imap);
        } else if (doDelete) {
          System.out.println("Deleting IMAP settings is not possible. Consider "
              + "disabling IMAP by updating it.\n");
        } else {
          settings.changeImap(users, Defaults.IMAP_ENABLE);
        }

      } else if (setting.startsWith("vacation")) {
        if (doGet) {
          Map<String, String> vacation = settings.retrieveVacation(destinationUser);
          System.out.println("vacation settings:");
          for (Entry<String, String> entry : vacation.entrySet())
            System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
        } else if (doDelete) {
          System.out.println("Deleting vacation settings is not possible. Consider "
              + "disabling the vacation auto-responder by updating it.\n");
        } else {
          settings.changeVacation(users, Defaults.VACATION_ENABLE, Defaults.VACATION_SUBJECT,
              Defaults.VACATION_MESSAGE, Defaults.VACATION_CONTACTS_ONLY);
        }

      } else if (setting.startsWith("signature")) {
        if (doGet) {
          String signature = settings.retrieveSignature(destinationUser);
          if (signature == null || signature.length() == 0) {
            System.out.println("No signature has been set.");
            return;
          }
          System.out.println("signature:");
          System.out.println("\tvalue: " + signature);
        } else if (doDelete) {
          System.out.println("Removing signature settings is not possible. "
              + "Consider changing the signature by updating it.\n");
        } else {
          settings.changeSignature(users, Defaults.SIGNATURE);
        }

      } else if (setting.startsWith("general")) {
        if (doGet) {
          System.out.println("Retrieving general settings is not supported.\n");
          printUsageAndExit();
        } else if (doDelete) {
          System.out.println("Deleting general settings is not possible.\n");
        } else {
          settings.changeGeneral(users,
              Defaults.GENERAL_PAGE_SIZE,
              Defaults.GENERAL_ENABLE_SHORTCUTS,
              Defaults.GENERAL_ENABLE_ARROWS,
              Defaults.GENERAL_ENABLE_SNIPPETS,
              Defaults.GENERAL_ENABLE_UNICODE);
        }

      } else if (setting.startsWith("language")) {
        if (doGet) {
          System.out.println("Retrieving language settings is not supported.\n");
          printUsageAndExit();
        } else if (doDelete) {
          System.out.println("Deleting language settings is not possible. Consider "
              + "changing the language by updating it.\n");
        } else {
          settings.changeLanguage(users, Defaults.LANGUAGE);
        }

      } else if (setting.startsWith("webclip")) {
        if (doGet) {
          System.out.println("Retrieving webclip settings is not supported.\n");
          printUsageAndExit();
        } else if (doDelete) {
          System.out.println("Deleting webclip settings is not possible. "
              + "Consider disabling webclip by updating it.\n");
        } else {
          settings.changeWebClip(users, Defaults.WEBCLIP_ENABLE);
        }

      } else if (setting.startsWith("delegation")) {
        if (doGet) {
          List<Map<String, String>> delegates = settings.retrieveEmailDelegates(destinationUser);
          if (delegates == null || delegates.size() == 0) {
            System.out.println("No email delegates found.");
            return;
          }
          int count = 0;
          for (Map<String, String> delegate : delegates) {
            System.out.println("delegate " + ++count + ":");
            Set<Entry<String, String>> entries = delegate.entrySet();
            for (Entry<String, String> entry : entries) {
              System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
            }
          }
        } else if (doDelete) {
          settings.deleteEmailDelegate(destinationUser, delegationEmailId);
        } else {
          settings.addEmailDelegate(destinationUser, delegationEmailId);
        }

      } else {
        printUsageAndExit();
      }
    } catch (AuthenticationException e) {
      System.err.println(e);
    } catch (IllegalArgumentException e) {
      System.err.println(e);
    } catch (ServiceException e) {
      System.err.println(e);
    } catch (MalformedURLException e) {
      System.err.println(e);
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}
