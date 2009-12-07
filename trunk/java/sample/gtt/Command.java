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
import com.google.gdata.util.ServiceException;

import java.io.IOException;

/**
 * The interface to be implemented by any class whose instances represent a
 * gdata request to the translator toolkit service.
 *
 * 
 */
public interface Command {

  /**
   * Executes the actions specified by the implementation using the given
   * GttService object.
   *
   * @param service An authenticated GttService object.
   * @param args arguments for the command, will be parsed using
   *        {@link sample.util.SimpleCommandLineParser}
   */
  public void execute(GttService service, String[] args)
      throws ServiceException, IOException;

  /**
   * @return a help string corresponding to this command.
   */
  public String helpString();
}
