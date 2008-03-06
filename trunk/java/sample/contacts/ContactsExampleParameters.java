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


package sample.contacts;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Keeps structured command line parameters for ContactsExample program.
 *
 * 
 */

public class ContactsExampleParameters {
  private static final String DEFAULT_FEED = "http://www.google.com/m8/feeds/";
  // Note! Tree Map is used to keep sequence of fields.
  private TreeMap<String, String> map;

  public enum ParameterNames {
    SCRIPT("script"),
    ACTION("action"),
    BASE_URL("base-url"),
    USERNAME("username"),
    PASSWORD("password"),
    SHOWDELETED("showdeleted"),
    UPDATED_MIN("updated-min"),
    OREDERBY("orderby"),
    SORTORDER("sortorder"),
    MAX_RESULTS("max-results"),
    START_INDEX("start-index"),
    HELP("help"),
    NAME("name"),
    NOTES("notes"),
    ID("id"),
    EMAIL("email",true),
    PHONE("phone",true),
    IM("im",true),
    VERBOSE("verbose"),
    ORGANIZATION("organization",true),
    POSTAL("postal",true),
    ;

    private final String parameterName;
    private boolean multipleParametersAllowed;

    ParameterNames(String parameterName, boolean multipleParametersAllowed){
      this.parameterName = parameterName;
      this.multipleParametersAllowed = multipleParametersAllowed;
    }

    ParameterNames(String parameterName) {
      this(parameterName,false);
    }

    public String getParameterName() {
      return parameterName;
    }

    public boolean isMultipleParametersAllowed() {
      return multipleParametersAllowed;
    }
  }
  /**
   * Constructor used in script case
   *
   * @param commandLineParams command line parameters
   * @param scriptLine line read from a file
   */
  public ContactsExampleParameters(ContactsExampleParameters commandLineParams,
      String scriptLine) {
    map = new TreeMap<String, String>();
    map.putAll(commandLineParams.map);
    fillMapFromArguments(scriptLine.split(" "));
  }

  /**
   * Constructor used by command line execution
   *
   * @param arguments arguments in form of array
   *
   */
  public ContactsExampleParameters(String arguments[]) {
    map = new TreeMap<String, String>();
    fillMapFromArguments(arguments);
  }


  private void fillMapFromArguments(String[] arguments) {
    for (String string : arguments) {
      if (string.startsWith("--")) {
        String param = string.substring(2);
        String params[] = param.split("=");
        if (params.length > 1) {
          map.put(params[0], params[1]);
        } else if (params.length == 1) {
          map.put(params[0], "");
        }
      }
    }
    verifyAllParameters();
  }

  /**
   * Verify if we understand all parameters
   */
  private void verifyAllParameters() {
    for (String parameter: map.keySet()) {
      verifyParameter(parameter);
    }
  }

  private void verifyParameter(String name) {
    for (ParameterNames parameter : ParameterNames.values()) {
      if (!parameter.isMultipleParametersAllowed()
          && name.equals(parameter.getParameterName())) {
        return;
      } else if (parameter.isMultipleParametersAllowed()
          && name.startsWith(parameter.getParameterName())) {
        return;
      }
    }
    throw new RuntimeException("Parameter " + name + " is not correct.");
  }

  String getScript() {
    return map.get(ParameterNames.SCRIPT.getParameterName());
  }

  String getAction() {
    return map.get(ParameterNames.ACTION.getParameterName());
  }

  String getBaseUrl() {
    String url = map.get(ParameterNames.BASE_URL.getParameterName());
    if (url == null) {
      url = DEFAULT_FEED;
    }
    return url;
  }

  String getUserName() {
    return map.get(ParameterNames.USERNAME.getParameterName());
  }

  String getPassword() {
    return map.get(ParameterNames.PASSWORD.getParameterName());
  }

  boolean isShowDeleted() {
    return (map.get(ParameterNames.SHOWDELETED.getParameterName()) != null);
  }

  String getUpdatedMin() {
    return map.get(ParameterNames.UPDATED_MIN.getParameterName());
  }

  String getOrderBy() {
    return map.get(ParameterNames.OREDERBY.getParameterName());
  }

  String getSortorder() {
    return map.get(ParameterNames.SORTORDER.getParameterName());
  }

  Integer getMaxResults() {
    String maxResString = map.get(ParameterNames.MAX_RESULTS.getParameterName());
    if (maxResString != null) {
      Integer val = Integer.parseInt(maxResString);
      if (val.intValue() < 1) {
        throw new RuntimeException(
            ParameterNames.MAX_RESULTS.getParameterName() + " should be > 0" );
      }
      return val;
    }
    return null;
  }

  Integer getStartIndex() {
    String startIndexString = map.get(ParameterNames.START_INDEX.getParameterName());
    if (startIndexString != null) {
      Integer val = Integer.parseInt(startIndexString);
      if (val.intValue() < 1) {
        throw new RuntimeException(
            ParameterNames.START_INDEX.getParameterName() + " should be > 0" );
      }
      return val;
    }
    return null;
  }

  boolean isHelp() {
    return (map.get(ParameterNames.HELP.getParameterName()) != null);
  }

  boolean isVerbose() {
    return (map.get(ParameterNames.VERBOSE.getParameterName()) != null);
  }

  String getName() {
    return map.get(ParameterNames.NAME.getParameterName());
  }

  String getNotes() {
    return map.get(ParameterNames.NOTES.getParameterName());
  }

  String getId() {
    return map.get(ParameterNames.ID.getParameterName());
  }

  void setId(String id) {
    map.put(ParameterNames.ID.getParameterName(), id);
  }

  LinkedList<String> getEmails() {
    LinkedList<String> emailList = new LinkedList<String>();
    for (String key : map.keySet()) {
      if (key.startsWith(ParameterNames.EMAIL.getParameterName())) {
        emailList.add(map.get(key));
      }
    }
    return emailList;
  }

  LinkedList<String> getPhones() {
    LinkedList<String> phoneList = new LinkedList<String>();
    for (String key : map.keySet()) {
      if (key.startsWith(ParameterNames.PHONE.getParameterName())) {
        phoneList.add(map.get(key));
      }
    }
    return phoneList;
  }

  LinkedList<String> getIms() {
    LinkedList<String> imList = new LinkedList<String>();
    for (String key : map.keySet()) {
      if (key.startsWith(ParameterNames.IM.getParameterName())) {
        imList.add(map.get(key));
      }
    }
    return imList;
  }

  LinkedList<String> getOrganizations() {
    LinkedList<String> organizationList = new LinkedList<String>();
    for (String key : map.keySet()) {
      if (key.startsWith(ParameterNames.ORGANIZATION.getParameterName())) {
        organizationList.add(map.get(key));
      }
    }
    return organizationList;
  }

  LinkedList<String> getPostal() {
    LinkedList<String> postalList = new LinkedList<String>();
    for (String key : map.keySet()) {
      if (key.startsWith(ParameterNames.POSTAL.getParameterName())) {
        postalList.add(map.get(key));
      }
    }
    return postalList;
  }

  int numberOfParameters() {
    return map.size();
  }

}
