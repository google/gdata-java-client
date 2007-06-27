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

package sample.gbase.recipe;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * Methods called from the JSP for generating HTML code.
 *
 * This is the work normally done by a web framework.
 */
public class DisplayUtils {
  
  /**
   * Prints checkboxes on two colums, with a text input field at the end.
   * The checked values will be displayed along with the values to be showed.
   * 
   * The text input field can be used to input a custom value. It has the
   * same name as the checkbox input fields, so when the submitted values
   * are processed, you have to remove the empty value, in case this field
   * remains empty.
   * 
   * @param out output writer
   * @param name name of the input field
   * @param values values to be shown
   * @param checked checked values
   * @throws IOException
   */
  public static void printCheckboxes(Writer out,
                                     String name,
                                     String[] values,
                                     Set<String> checked) throws IOException {
    Set<String> allValues = new TreeSet<String>();
    // The displayed values are the static values plus the checked values
    allValues.addAll(Arrays.asList(values));
    if (checked == null) {
      checked = new HashSet<String>();
    }
    allValues.addAll(checked);
    int middle = (allValues.size() + 2) / 2;
    int row = 0;
    out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
    out.write("<tr><td><ul class=\"inputlist\">");
    for (String value : allValues) {
      if (row == middle) {
        out.write("</ul></td><td><ul class=\"inputlist\">");
      }
      out.write("<li>");
      printCheckbox(out, name, value, checked.contains(value));
      out.write("</li>");
      row++;
    }
    out.write("<li><label>Other:<br/><input id=\"other");
    out.write(name);
    out.write("\" type=\"text\" name=\"");
    out.write(name);
    out.write("\" value=\"\" size=\"15\" class=\"txt\"></label></li>");
    out.write("</ul></td></tr></table>");
  }
  
  /**
   * Prints a checkbox input field with a label.
   * 
   * @param out output writer
   * @param name name of the field
   * @param value value of the field
   * @param checked true if the field is checked
   * @throws IOException
   */
  public static void printCheckbox(Writer out, String name, String value, 
      boolean checked) throws IOException {
    value = escape(value);
    out.write("<label><input type=\"checkbox\" name=\"");
    out.write(name);
    out.write("\" value=\"");
    out.write(value);
    out.write("\"");
    if (checked) {
      out.write(" checked");
    }
    out.write("/>&nbsp;");
    if (checked) {
      out.write("<b>");
    }
    out.write(value);
    if (checked) {
      out.write("</b>");
    }
    out.write("</label>");
  }

  /**
   * Escapes the items of the Collection and returns them in a StringBuffer,
   * separated by commas.
   * 
   * @param list
   * @return list of the elements of the collection, sepparated by commas
   */
  public static StringBuffer printList(Collection<String> list) {
    StringBuffer sb = new StringBuffer();
    if (list != null && list.size() > 0) {
      Iterator<String> iter = list.iterator();
      while (true) {
        sb.append(escape(iter.next()));
        if (iter.hasNext()) {
          sb.append(", ");
        } else {
          break;
        }
      }
    }
    return sb;
  }

  /**
   * Prints &lt;option&gt; tags and set the selected option.
   * 
   * @param out output writer
   * @param names option names, which are used both for 
   *   values and for labels
   * @param current name of the current option
   * @throws IOException
   */
  public static void printOptions(Writer out,
                                  String[] names,
                                  String current) throws IOException {
    for (int i=0; i<names.length; i++) {
      String name = names[i];
      printOption(out, name, name, name.equals(current));
    }
    printOption(out, "all", "", current==null);
  }

  /**
   * Prints one &lt;option&gt; tag and select it if necessary.
   * 
   * @param out output writer
   * @param label option label
   * @param value option value
   * @param selected true if it's selected
   * @throws IOException
   */
  public static void printOption(Writer out,
                                 String label,
                                 String value,
                                 boolean selected) throws IOException {
    out.write("<option");
    if (selected) {
      out.write(" selected");
    }
    out.write(" value=\"");
    out.write(value);
    out.write("\">");
    out.write(escape(label));
    out.write("</option>");
  }
  
  /**
   * Simple HTML escaping of the String version of the specified Object.
   * 
   * @param obj Object that has a meaningful toString() value
   * @return string with some escaped characters
   */
  public static String escape(Object obj) {
    if (obj == null) {
      return "";
    }
    return escapeAndShorten(obj.toString(), -1);
  }

  /**
   * Simple HTML escaping.
   * 
   * Escapes &lt; &amp; and &gt; and leaves the rest as it is.
   * 
   * @param raw
   * @return string with some escaped characters
   */
  public static String escape(String raw) {
    return escapeAndShorten(raw, -1);
  }

  /**
   * Escape HTML data and shorted the result if necessary.
   * 
   * If the text is escaped, &lt;b&gt;...&lt;/b&gt; will be 
   * appended. 
   * 
   * @param raw raw text to escape 
   * @param maxLength maximum output length 
   * @return HTML code
   */
  public static String escapeAndShorten(String raw, int maxLength) {
    if (raw == null) {
      return "";
    }
    StringBuilder retval = new StringBuilder();
    int length = raw.length();
    boolean shortened = false;
    if (maxLength != -1 && length > maxLength) {
      length = maxLength;
      shortened = true;
    }
    for (int i=0; i<length; i++) {
      char c = raw.charAt(i);
      switch (c) {
        case '<':
          retval.append("&lt;");
          break;
        case '>':
          retval.append("&gt;");
          break;
        case '&':
          retval.append("&amp;");
          break;
        case '\'':
          retval.append("&#039;");
          break;
        case '"': 
          retval.append("&#034;");
          break;
        default:
          retval.append(c);
          break;
      }
    }
    if (shortened) {
      retval.append("<b>...</b>");
    }
    return retval.toString();
  }

}
