/* Copyright (c) 2006 Google Inc.
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

package sample.gbase.cmdline;

import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * Creates and initializes commands given command-line
 * arguments.
 *
 * This class is totally useless if you want to understand
 * how the API works. All it does is parse command-line
 * arguments and call some setters on the different <code>*Command</code>
 * objects. Have a look at the <code>*Command</code> classes instead.
 */
class CommandFactory {

  /**
   * All commands available in this system, for producing error messages.
   */
  private static final String ALL_COMMANDS =
      "query, get, update, insert, delete, batch";

  /**
   * Creates a {@link Command} object and initializes it using
   * command-line arguments.
   *
   * @param args command-line arguments
   * @return a new Command object, properly initialized and ready to use
   */
  public static Command createCommand(String[] args) {
    if (args.length == 0) {
      throw error("Please give first the command you want to run (" +
                  ALL_COMMANDS + ") then the parameters for that " +
                  "command.");
    }

    String commandName = args[0];
    if ("query".equals(commandName)) {
      return createQueryCommand(args);
    } else if("insert".equals(commandName)) {
      return createInsertCommand(args);
    } else if("update".equals(commandName)) {
      return createUpdateCommand(args);
    } else if("delete".equals(commandName)) {
      return createDeleteCommand(args);
    } else if("get".equals(commandName)) {
      return createGetCommand(args);
    } else if("batch".equals(commandName)) {
      return createBatchCommand(args);
    } else if("query-media".equals(commandName)) {
      return createQueryMediaCommand(args);
    } else if("insert-media".equals(commandName)) {
      return createInsertMediaCommand(args);      
    } else if("update-media".equals(commandName)) {
      return createUpdateMediaCommand(args);      
    } else if("delete-media".equals(commandName)) {
      return createDeleteMediaCommand(args);      
    } else if("get-media".equals(commandName)) {
      return createGetMediaCommand(args);      
    } else {
      throw error("Unknown command: " + commandName +
                  ". Available commands: " + ALL_COMMANDS);
    }
  }

  /**
   * Creates and initializes a {@link QueryCommand}.
   */
  private static Command createQueryCommand(String[] args) {
    QueryCommand command = new QueryCommand();
    args = parseAndSetCommonArguments(command, args);

    if (args.length == 1) {
      command.setQuery(args[0]);
    } else if (args.length > 1) {
      throw error("Expected at most one query argument, got " + args.length);
    }
    return command;
  }

  /**
   * Creates and initializes a {@link InsertCommand}.
   */
  private static Command createInsertCommand(String[] args) {
    InsertCommand command = new InsertCommand();
    args = parseAndSetCommonArguments(command, args);
    expectNoMoreArguments(args);

    return command;
  }

  /**
   * Creates and initializes a {@link BatchCommand}.
   */
  private static Command createBatchCommand(String[] args) {
    BatchCommand command = new BatchCommand();
    args = parseAndSetCommonArguments(command, args);
    expectNoMoreArguments(args);

    return command;
  }

  /**
   * Creates and initializes a {@link UpdateCommand}.
   */
  private static Command createUpdateCommand(String[] args) {
    UpdateCommand command = new UpdateCommand();
    args = parseAndSetCommonArguments(command, args);

    command.setItemId(getItemId(args));

    return command;
  }

  /**
   * Creates and initializes a {@link DeleteCommand}.
   */
  private static Command createDeleteCommand(String[] args) {
    DeleteCommand command = new DeleteCommand();
    args = parseAndSetCommonArguments(command, args);
    
    command.setItemId(getItemId(args));

    return command;
  }

  /**
   * Creates and initializes a {@link GetCommand).
   */
  private static Command createGetCommand(String[] args) {
    GetCommand command = new GetCommand();
    args = parseAndSetCommonArguments(command, args);
    
    command.setItemId(getItemId(args));

    return command;
  }  
  
  /**
   * Creates and initializes a {@link QueryMediaCommand}.
   */
  private static Command createQueryMediaCommand(String[] args) {
    QueryMediaCommand command = new QueryMediaCommand();
    args = parseAndSetCommonArguments(command, args);

    if (args.length == 1) {
      command.setItemMediaUrl(args[0]);
    } else {
      throw error("Expected [item_media_feed_url], got " + Arrays.toString(args));
    }
    
    return command;
  }

  /**
   * Creates and initializes a {@link InsertMediaCommand}.
   */
  private static Command createInsertMediaCommand(String[] args) {
    InsertMediaCommand command = new InsertMediaCommand();
    args = parseAndSetCommonArguments(command, args);
    
    if (args.length < 3 || args.length > 4) {
      throw error("Expected four arguments: [item_media_feed_url attachment_path_to_file " +
      		"attachment_mime_type caption?], got " + Arrays.toString(args));
    }
    
    command.setItemMediaUrl(args[0]);
    command.setAttachmentFile(args[1]);
    command.setAttachmentMimeType(args[2]);
    if (args.length == 4) {
      command.setCaption(args[3]);
    }

    return command;
  }

  /**
   * Creates and initializes a {@link UpdateMediaCommand}.
   */
  private static Command createUpdateMediaCommand(String[] args) {
    UpdateMediaCommand command = new UpdateMediaCommand();
    args = parseAndSetCommonArguments(command, args);

    command.setAttachmentId(getItemId(args));

    return command;
  }

  /**
   * Creates and initializes a {@link DeleteMediaCommand}.
   */
  private static Command createDeleteMediaCommand(String[] args) {
    DeleteMediaCommand command = new DeleteMediaCommand();
    args = parseAndSetCommonArguments(command, args);

    command.setAttachmentId(getItemId(args));

    return command;
  }

  /**
   * Creates and initializes a {@link GetMediaCommand).
   */
  private static Command createGetMediaCommand(String[] args) {
    GetMediaCommand command = new GetMediaCommand();
    args = parseAndSetCommonArguments(command, args);
    
    command.setAttachmentId(getItemId(args));

    return command;
  }
  
  /**
   * Get the item id from the command line and make sure it's the
   * only argument left.
   *
   * @param args command line arguments left over from
   *   {@link #parseAndSetCommonArguments(Command, String[])}
   * @return the item ID (an url)
   */
  private static String getItemId(String[] args) {
    if (args.length != 1) {
      throw error("Expected one argument after the command name: " +
                  "the ID (url) of the item to delete.");
    }
    return args[0];
  }

  /**
   * Parse the command line and initialize things that are common
   * to all {@link Command}s.
   *
   * @param command the command object
   * @param args command-line arguments (0 is the command name)
   * @return the arguments that are left that haven't been parsed
   *   by this method, or an empty array if there's nothing left
   */
  private static String[] parseAndSetCommonArguments(Command command,
                                                     String[] args) {
    // Start at the first argument after the command name
    int current = 1;

    while (current < args.length && args[current].startsWith("-")) {
      String option = args[current];
      current++;
      if ( current >= args.length) {
        throw error("expected an argument after option " + option);
      }
      if ("--dry_run".equals(option)) {
        command.setDryRun(true);
        continue;
      }
      String value = args[current];
      current++;

      if ("--user".equals(option)) {
        command.setUsername(value);
      } else if("--password".equals(option)) {
        command.setPassword(value);
      } else if("--url".equals(option)) {
        try {
          command.setGoogleBaseServerUrl(value);
        } catch(MalformedURLException e) {
          throw error("Value for --url should be a valid URL: " + 
              e.getMessage());
        }
      } else if("--auth".equals(option)) {
        try {
          command.setAuthenticationServerUrl(value);
        } catch(MalformedURLException e) {
          throw error("Value for --auth should be a valid http or https " +
                      "URL: " + e.getMessage());
        }
      } else if("--key".equals(option)) {
        command.setKey(value);
      } else {
        throw error("unexpected option: " + option);
      }
    }

    if (!command.hasAllIdentificationInformation()) {
      throw error("You must input all two required parameters: " +
                  "--user email --password password ");
    }

    // leave the rest for the command
    String[] retval = new String[args.length-current];
    System.arraycopy(args, current, retval, 0, retval.length);
    return retval;
  }

  private static void expectNoMoreArguments(String[] args) {
    if (args.length > 0) {
      throw error("Expected no more arguments, got instead " + args[0]);
    }
  }

  private static IllegalArgumentException error(String message) {
    return new IllegalArgumentException("Error: wrong arguments. " + message);
  }
}
