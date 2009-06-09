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


package sample.contacts;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Keeps structured command line parameters for ContactsExample program.
 * 
 * 
 * 
 */

public class ContactsExampleParameters {
  /**
   * Actions that can be executed with the sample application.
   * They are all lowercase because they are passed as parameters
   * and then converted using valueOf() method.
   */
  public enum Actions  {
    LIST,
    QUERY,
    ADD,
    DELETE,
    UPDATE;
  }

  private static final String DEFAULT_FEED = "http://www.google.com/m8/feeds/";
  private static final String DEFAULT_PROJECTION = "thin";

  /**
   * Keeps map of parameter -> value.
   * Note! Tree Map is used to keep sequence of fields.
   */
  private SortedMap<String, String> parameterValueMap =
    new TreeMap<String,String>();

  /**
   * List of command line parameters likely to be the description of an
   * element.
   * Those parameters are added to the list, what are not recognized as a valid 
   * command line argument (nor an action or option).
   * These parameters are later parsed by {@link ElementParser} and processed by 
   * {@link ElementHelper}. 
   */
  private List<String> elementDesc = new LinkedList<String>();

  /**
   * Stores names of parameters.
   */
  public enum ParameterNames {
    SCRIPT("script"),
    ACTION("action"),
    BASE_URL("base-url"),
    USERNAME("username"),
    PASSWORD("password"),
    CONTACTFEED("contactfeed"),
    GROUPFEED("groupfeed"),
    SHOWDELETED("showdeleted"),
    REQUIRE_ALL_DELETED("require-all-deleted"),
    UPDATED_MIN("updated-min"),
    OREDERBY("orderby"),
    SORTORDER("sortorder"),
    MAX_RESULTS("max-results"),
    START_INDEX("start-index"),
    HELP("help"),
    PROJECTION("projection"),
    VERBOSE("verbose"),
    ID("id"),
    GROUP("querygroupid"),
    ;

    private final String parameterName;

    ParameterNames(String parameterName) {
      this.parameterName = parameterName;
    }

    public String getParameterName() {
      return parameterName;
    }

  }

  /**
   * Constructor used in case script parameter is used.
   *
   * @param commandLineParams command line parameters
   * @param scriptLine line read from a file
   */
  public ContactsExampleParameters(ContactsExampleParameters commandLineParams,
      String scriptLine) {
    parameterValueMap.putAll(commandLineParams.parameterValueMap);
    elementDesc.addAll(commandLineParams.elementDesc);
    fillFromArguments(scriptLine.split(" "));
  }

  /**
   * Constructor used when no script is passed at command line
   * only the command line parameters are used.
   *
   * @param arguments arguments in form of array
   */
  public ContactsExampleParameters(String arguments[]) {
    fillFromArguments(arguments);
  }

  /**
   * Parse arguments.
   * 
   * @param arguments arguments in form of array
   */
  private void fillFromArguments(String[] arguments) {
    for (String string : arguments) {
      if (!string.startsWith("--")) {
        throw new IllegalArgumentException("illegal parameter: " + string);
      }
      String param = string.substring(2);
      String params[] = param.split("=", 2);
      boolean found = false;
      for (ParameterNames parameterName : ParameterNames.values()) {
        String name = parameterName.getParameterName().toLowerCase();
        if (name.equals(params[0])) {
          if (params.length == 1) {
            parameterValueMap.put(params[0], "");
          } else {
            parameterValueMap.put(params[0], params[1]);
          }
          found = true;
          break;
        }
      }
      if (!found) {
        elementDesc.add(string);
      }
    }
    verifyAllParameters();    
  }

  /**
   * Verify if we understand all parameters.
   *
   * @throws IllegalArgumentException in case there
   * is a parameter which is not expected.
   */
  private void verifyAllParameters() {
    for (String parameter : parameterValueMap.keySet()) {
      verifyParameter(parameter);
    }
  }

  private void verifyParameter(String name) {
    for (ParameterNames parameter : ParameterNames.values()) {
      if (name.equals(parameter.getParameterName())) {
        return;
      }
    }
    throw new IllegalArgumentException(
        "Parameter " + name + " is not correct.");
  }
  
  String getParameter(ParameterNames parameters) {
    return parameterValueMap.get(parameters.getParameterName());
  }

  String getScript() {
    return getParameter(ParameterNames.SCRIPT);
  }

  Actions getAction() {
    String actionString = getParameter(ParameterNames.ACTION);
    if (actionString == null) {
      return null;
    }
    return Actions.valueOf(actionString.toUpperCase());
  }

  String getBaseUrl() {
    String url = getParameter(ParameterNames.BASE_URL);
    if (url == null) {
      url = DEFAULT_FEED;
    }
    return url;
  }

  String getUserName() {
    return getParameter(ParameterNames.USERNAME);
  }

  String getPassword() {
    return getParameter(ParameterNames.PASSWORD);
  }
  
  boolean isContactFeed() {
    return getParameter(ParameterNames.CONTACTFEED) != null;
  }

  boolean isGroupFeed() {
    return getParameter(ParameterNames.GROUPFEED) != null;
  }
  
  boolean isShowDeleted() {
    return getParameter(ParameterNames.SHOWDELETED) != null;
  }

  String getRequireAllDeleted() {
    return getParameter(ParameterNames.REQUIRE_ALL_DELETED);
  }

  String getUpdatedMin() {
    return getParameter(ParameterNames.UPDATED_MIN);
  }

  String getOrderBy() {
    return getParameter(ParameterNames.OREDERBY);
  }

  String getSortorder() {
    return getParameter(ParameterNames.SORTORDER);
  }

  Integer getMaxResults() {
    String maxResString = getParameter(ParameterNames.MAX_RESULTS);
    if (maxResString != null) {
      Integer val = Integer.parseInt(maxResString);
      if (val.intValue() < 1) {
        throw new RuntimeException(
            ParameterNames.MAX_RESULTS + " should be > 0" );
      }
      return val;
    }
    return null;
  }

  Integer getStartIndex() {
    String startIndexString = getParameter(ParameterNames.START_INDEX);
    if (startIndexString != null) {
      Integer val = Integer.parseInt(startIndexString);
      if (val.intValue() < 1) {
        throw new RuntimeException(
            ParameterNames.START_INDEX + " should be > 0" );
      }
      return val;
    }
    return null;
  }

  boolean isHelp() {
    return (getParameter(ParameterNames.HELP) != null);
  }

  boolean isVerbose() {
    return (getParameter(ParameterNames.VERBOSE) != null);
  }

  String getId() {
    return getParameter(ParameterNames.ID);
  }

  void setId(String id) {
    parameterValueMap.put(ParameterNames.ID.getParameterName(), id);
  }
  
  String getProjection() {
    String projection = getParameter(ParameterNames.PROJECTION);
    if (projection == null) {
      projection = DEFAULT_PROJECTION;
    }
    return projection;
  }
  
  String getGroup() {
    return getParameter(ParameterNames.GROUP);
  }

  List<String> getElementDesc() {
    return elementDesc;
  }
  
  int numberOfParameters() {
    return parameterValueMap.size();
  }
}
