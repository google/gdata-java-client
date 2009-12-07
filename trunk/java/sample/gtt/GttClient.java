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


package sample.gtt;

import com.google.common.collect.ImmutableMap;
import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Contains utility methods to connect to the Google Translator Toolkit (Gtt)
 * service and list/update/download/delete/share
 * documents/translation-memories/glossaries using the Google Data API's
 * java client library as the interface.
 *
 */
public class GttClient {

  private static final String USER_PROMPT
      = "\nPlease enter a command or Type 'help' for list of commands."
          + "\nGoogle Translator Toolkit >";

  private static final Map<String, GttCommand> NAME_TO_COMMAND_MAP;
  static {
    ImmutableMap.Builder<String, GttCommand> builder = ImmutableMap.builder();
    for (GttCommand command : GttCommand.values()) {
      builder.put(command.name().toLowerCase(), command);
    }
    NAME_TO_COMMAND_MAP = builder.build();
  }

  /**
   * Prevent the creation of utility class object.
   */
  private GttClient() {
  }

  /**
   * Uses the command line arguments to authenticate the GoogleService and build
   * the basic feed URI, then invokes all the other methods to demonstrate how
   * to interface with the Translator toolkit service.
   *
   * @param args See the usage method.
   */
  public static void main(String[] args) {
    try {
      // Connect to the Google translator toolkit service
      GttService service
          = new GttService("sample.gtt.GttClient");

      // Login if there is a command line argument for it.
      if (args.length >= 1
          && NAME_TO_COMMAND_MAP.get(args[0]) == GttCommand.LOGIN) {
        GttCommand.LOGIN.execute(service, args);
      }

      // Input stream to get user input
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      // Do some actions based on user input
      while (true) {
        // Get user input
        System.out.print(USER_PROMPT);
        System.out.flush();
        String userInput = in.readLine();
        System.out.println();

        // Do action corresponding to user input
        String[] commandArgs = userInput.split("\\s+");
        GttCommand command = NAME_TO_COMMAND_MAP.get(commandArgs[0]);
        if (command != null) {
          command.execute(service, commandArgs);
        } else {
          System.out.println("Sorry I did not understand that.");
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * List of commands show-cased in this sample application talking to Google
   * Translator toolkit service.
   */
  public enum GttCommand implements Command {
    /**
     * Command to login to Google Translator toolkit.
     */
    LOGIN(new Command() {
      public void execute(GttService gttService, String[] args)
          throws ServiceException {
        // Get username, password and feed URI from command-line arguments.
        SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
        String userName = parser.getValue("username", "user", "u");
        String userPassword = parser.getValue("password", "pass", "p");

        if (parser.containsKey("baseuri")) {
          FeedUris.setBaseUrl(parser.getValue("baseuri"));
        }

        if (userName == null || userPassword == null) {
          System.out.println(helpString());
        } else {
          // Authenticate using ClientLogin
          gttService.setUserCredentials(userName, userPassword);
          System.out.println("\nYou're now logged in as " + userName + "!");
        }
      }

      public String helpString() {
        return "Logs in to Google Translator toolkit with new credentials."
            + "\n\t--username <username>\t; Required parameter"
            + "\n\t--password <password>\t; Required parameter"
            + "\n\t--baseuri <uri>\t; "
            + "Optional, default is http://translate.google.com/toolkit/feeds";
      }
    }),

    /**
     * Command to list document in Google Translator toolkit inbox.
     */
    LIST_DOCS(ListDocumentsCommand.INSTANCE),

    /**
     * Command to list translation memories.
     */
    LIST_TMS(ListTranslationMemoriesCommand.INSTANCE),

    /**
     * Command to list glossaries.
     */
    LIST_GLOSSARIES(ListGlossariesCommand.INSTANCE),

    /**
     * Command to create documents for translation.
     */
    UPLOAD_DOC(UploadDocumentCommand.INSTANCE),

    /**
     * Command to create translation memories.
     */
    ADD_TM(AddTranslationMemoryCommand.INSTANCE),

    /**
     * Command to create glossaries.
     */
    ADD_GLOSSARY(AddGlossaryCommand.INSTANCE),

    /**
     * Command to delete documents.
     */
    DELETE_DOC(DeleteDocumentCommand.INSTANCE),

    /**
     * Command to delete translation memories.
     */
    DELETE_TM(DeleteTranslationMemoryCommand.INSTANCE),

    /**
     * Command to delete glossaries.
     */
    DELETE_GLOSSARY(DeleteGlossaryCommand.INSTANCE),

    /**
     * Updates documents.
     */
    UPDATE_DOC(UpdateDocumentCommand.INSTANCE),

    /**
     * Updates translation memory.
     */
    UPDATE_TM(UpdateTranslationMemoryCommand.INSTANCE),

    /**
     * Updates glossary.
     */
    UPDATE_GLOSSARY(UpdateGlossaryCommand.INSTANCE),

    /**
     * Updates document sharing.
     */
    DOC_SHARING(ShareCommand.DOCUMENTS_INSTANCE),

    /**
     * Updates translation memory sharing.
     */
    TM_SHARING(ShareCommand.TMS_INSTANCE),

    /**
     * Updates glossary sharing.
     */
    GLOSSARY_SHARING(ShareCommand.GLOSSARIES_INSTANCE),

    /**
     * Downloads a translated document.
     */
    DOWNLOAD_DOC(DownloadDocumentCommand.INSTANCE),


    /**
     * Command to exit the command line interface.
     */
    EXIT(new Command() {
      public void execute(GttService gttService, String[] args) {
        System.out.println(".....thanks for using the Google translator "
            + "toolkit sample gdata app. See you again.");
        System.exit(0);
      }

      public String helpString() {
        return "Exits the program.";
      }
    }),

    /**
     * Command to print out commands list or help text of other commands.
     */
    HELP(new Command() {
      public void execute(GttService gttService, String[] args) {
        if (args.length > 1 && NAME_TO_COMMAND_MAP.containsKey(args[1])) {
          System.out.println(NAME_TO_COMMAND_MAP.get(args[1]).helpString());
        } else {
          // Print out the list of commands
          for (Map.Entry<String, GttCommand> command
               : NAME_TO_COMMAND_MAP.entrySet()) {
            System.out.println(command.getKey() + "\t; "
                + command.getValue().helpString());
          }
        }
      }

      public String helpString() {
        return "Type 'help <command>' for information about a specific "
            + "command";
      }
    });

    private final Command delegate;

    GttCommand(Command delegate) {
      this.delegate = delegate;
    }

    public void execute(GttService gttService, String[] args) {
      try {
        delegate.execute(gttService, args);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("\nOops!!! There was some problem executing your "
            + "request.");
      }
    }

    public String helpString() {
      return delegate.helpString();
    }
  }
}
