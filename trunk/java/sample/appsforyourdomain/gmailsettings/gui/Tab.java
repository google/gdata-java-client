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


package sample.appsforyourdomain.gmailsettings.gui;

import javax.swing.JPanel;

/**
 * Stores some common information for the tabs.
 */
public class Tab extends JPanel {

  protected String name;
  protected String tooltip;

  /**
   * @param name The name to assign to the tab.
   * @param tooltip The tooltip to be used for the tab.
   */
  public Tab(String name, String tooltip) {
    setName(name);
    setTooltip(tooltip);
  }

  /**
   * @Return the name of the tab
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of the tab.
   *
   * @param name The name to assign to the tab.
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return The current value for tooltip.
   */
  public String getTooltip() {
    return this.tooltip;
  }

  /**
   * Sets the tooltop for the tab.
   *
   * @param tooltip The tooltip to be used for the tab.
   */
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }
}
