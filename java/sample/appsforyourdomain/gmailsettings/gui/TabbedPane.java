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

import javax.swing.JTabbedPane;

/**
 * Handles adding all the tabs needed.
 */
public class TabbedPane extends JTabbedPane {

  protected Tab authenticationTab;
  protected Tab signatureTab;
  protected Tab labelTab;
  protected Tab filtersTab;
  protected Tab sendasTab;
  protected Tab popTab;
  protected Tab forwardingTab;
  protected Tab imapTab;
  protected Tab vacationTab;
  protected Tab languageTab;
  protected Tab generalTab;
  protected Tab webClipTab;

  /**
   * Create an instance of all tabs required, and add them to the pane.
   */
  public TabbedPane() {
    super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    authenticationTab = new TabAuthentication();
    signatureTab = new TabSignature();
    labelTab = new TabLabel();
    filtersTab = new TabFilter();
    sendasTab = new TabSendAs();
    popTab = new TabPop();
    forwardingTab = new TabForwarding();
    imapTab = new TabImap();
    vacationTab = new TabVacation();
    languageTab = new TabLanguage();
    generalTab = new TabGeneral();
    webClipTab = new TabWebClip();

    addTab(authenticationTab);
    addTab(signatureTab);
    addTab(labelTab);
    addTab(filtersTab);
    addTab(sendasTab);
    addTab(popTab);
    addTab(forwardingTab);
    addTab(imapTab);
    addTab(vacationTab);
    addTab(languageTab);
    addTab(generalTab);
    addTab(webClipTab);

    this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
  }

  /**
   * Helper method for adding tabs to the pane.
   *
   * @param tab The tab to be added to the JTabbedPane
   */
  protected void addTab(Tab tab) {
    super.addTab(tab.getName(), null, tab, tab.getTooltip());
  }
}
