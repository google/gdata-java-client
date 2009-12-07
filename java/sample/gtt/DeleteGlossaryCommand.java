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

import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Deletes glossaries.
 *
 * 
 */
public class DeleteGlossaryCommand implements Command {

  public static final DeleteGlossaryCommand INSTANCE
      = new DeleteGlossaryCommand();

  /**
   * This is a singleton.
   */
  private DeleteGlossaryCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String id = parser.getValue("id");
    URL feedUrl = FeedUris.getGlossaryFeedUrl(id);

    System.out.print("Deleting glossary with id: " + id + " ....");
    System.out.flush();

    service.delete(feedUrl);

    System.out.println("...done");
  }

  public String helpString() {
    return "Deletes specified glossary."
        + "\n\t--id <id>\t; id of the glossary to delete";
  }
}
