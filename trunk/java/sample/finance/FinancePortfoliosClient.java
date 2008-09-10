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


package sample.finance;

import com.google.gdata.client.finance.FinanceService;
import com.google.gdata.client.finance.FinanceUtilities;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.Money;
import com.google.gdata.data.finance.PortfolioData;
import com.google.gdata.data.finance.PortfolioEntry;
import com.google.gdata.data.finance.PortfolioFeed;
import com.google.gdata.data.finance.PositionData;
import com.google.gdata.data.finance.PositionEntry;
import com.google.gdata.data.finance.PositionFeed;
import com.google.gdata.data.finance.TransactionData;
import com.google.gdata.data.finance.TransactionEntry;
import com.google.gdata.data.finance.TransactionFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Demonstrates interactions with Google Finance Data API's portfolio feeds
 * using the Java client library:
 *
 * <ul>
 * <li>Retrieve all of the user's portfolios</li>
 * <li>Retrieve a single user portfolio</li>
 * <li>Create a new portfolio</li>
 * <li>Rename an existing portfolio</li>
 * <li>Delete an existing portfolio</li>
 * <li>Retrieve all of the positions belonging to a user portfolio</li>
 * <li>Retrieve a single position belonging to a user portfolio</li>
 * <li>Create a new position</li>
 * <li>Delete an existing position</li>
 * <li>Retrieve all of the transactions belonging to a user position</li>
 * <li>Retrieve a single transaction belonging to a user position</li>
 * <li>Create a new transaction</li>
 * <li>Update an existing transaction</li>
 * <li>Delete an existing transaction</li>
 * </ul>
 */
public class FinancePortfoliosClient {

  private enum MainCommands {
    PORTFOLIOS,
    POSITIONS,
    TRANSACTIONS,
    HELP,
    QUIT
  }

  private enum PortfolioCommands {
    QUERY_FEED,
    QUERY_ENTRY,
    CREATE,
    UPDATE,
    DELETE,
    HELP,
    BACK,
    POSITIONS,
    QUIT
  }

  private enum PositionCommands {
    QUERY_FEED,
    QUERY_ENTRY,
    CREATE,
    DELETE,
    HELP,
    BACK,
    TRANSACTIONS,
    QUIT
  }

  private enum TransactionCommands {
    QUERY_FEED,
    QUERY_ENTRY,
    CREATE,
    UPDATE,
    DELETE,
    HELP,
    BACK,
    QUIT
  }

  // User's email and password:
  private static String userEmail = "";
  private static String userPassword = "";

  // Base URL for GData requests
  // GData Server is supplied as command-line argument and
  // appended with /finance/feeds/default
  private static String server = "http://finance.google.com";
  private static String basePath = "/finance/feeds/";
  private static String baseUrl = server + basePath + "default";

  // Feed and Entry URI suffixes:
  private static final String PORTFOLIO_FEED_URL_SUFFIX = "/portfolios";
  private static final String POSITION_FEED_URL_SUFFIX = "/positions";
  private static final String TRANSACTION_FEED_URL_SUFFIX = "/transactions";

  /**
   * Portfolio ID, Ticker, and Transaction ID are components of the
   * heirarchical feed URL:
   * e.g. The Portfolio Feed
   * http://finance.google.com/finance/feeds/default/portfolios
   * e.g. A Portfolio Entry
   * http://finance.google.com/finance/feeds/default/portfolios/<pid>
   * e.g. A Position Feed
   * http://finance.google.com/finance/feeds/default/portfolios/<pid>
   *     /positions
   * e.g. A Position Entry
   * http://finance.google.com/finance/feeds/default/portfolios/<pid>
   *     /positions/<ticker>
   * e.g. A Transaction Feed
   * http://finance.google.com/finance/feeds/default/portfolios/<pid>
   *     /positions/<ticker>/transactions
   * e.g. A Transaction Entry
   * http://finance.google.com/finance/feeds/default/portfolios/<pid>
   *     /positions/<ticker>/transactions/<tid>
   * (where pid (portfolioIdProperty) and tid (transactionIdProperty) are
   * 1, 2, 3, ... and a ticker is of the form NASDAQ:GOOG)
   */
  private static String portfolioIdProperty;
  private static String tickerProperty;
  private static String transactionIdProperty;

  /**
   * Processes commands entered at the main menu.
   *
   * @param cmd user entered command
   * @return type of command
   */
  private static MainCommands processMainMenuCommand(String cmd) {
    if (cmd.equals("portfolios") || cmd.equals("pr")) {
      return FinancePortfoliosClient.MainCommands.PORTFOLIOS;
    } else if (cmd.equals("positions") || cmd.equals("ps")) {
      return FinancePortfoliosClient.MainCommands.POSITIONS;
    } else if (cmd.equals("transactions") || cmd.equals("t")) {
      return FinancePortfoliosClient.MainCommands.TRANSACTIONS;
    } else if (cmd.equals("quit") || cmd.equals("q")) {
      return FinancePortfoliosClient.MainCommands.QUIT;
    } else {
      return FinancePortfoliosClient.MainCommands.HELP;
    }
  }

  /**
   * Processes commands entered at the portfolio menu.
   *
   * @param cmd user entered command
   * @return type of command
   */
  private static PortfolioCommands processPortfolioMenuCommand(String cmd) {
    if (cmd.equals("list") || cmd.equals("l")) {
      return FinancePortfoliosClient.PortfolioCommands.QUERY_FEED;
    } else if (cmd.equals("show") || cmd.equals("s")) {
      return FinancePortfoliosClient.PortfolioCommands.QUERY_ENTRY;
    } else if (cmd.equals("create") || cmd.equals("c")) {
      return FinancePortfoliosClient.PortfolioCommands.CREATE;
    } else if (cmd.equals("update") || cmd.equals("u")) {
      return FinancePortfoliosClient.PortfolioCommands.UPDATE;
    } else if (cmd.equals("delete") || cmd.equals("d")) {
      return FinancePortfoliosClient.PortfolioCommands.DELETE;
    } else if (cmd.equals("back") || cmd.equals("b")) {
      return FinancePortfoliosClient.PortfolioCommands.BACK;
    } else if (cmd.equals("positions") || cmd.equals("p")) {
      return FinancePortfoliosClient.PortfolioCommands.POSITIONS;
    } else if (cmd.equals("quit") || cmd.equals("q")) {
      return FinancePortfoliosClient.PortfolioCommands.QUIT;
    } else {
      return FinancePortfoliosClient.PortfolioCommands.HELP;
    }
  }

  /**
   * Processes commands entered at the position menu.
   *
   * @param cmd user entered command
   * @return type of command
   */
  private static PositionCommands processPositionMenuCommand(String cmd) {
    if (cmd.equals("list") || cmd.equals("l")) {
      return FinancePortfoliosClient.PositionCommands.QUERY_FEED;
    } else if (cmd.equals("show") || cmd.equals("s")) {
      return FinancePortfoliosClient.PositionCommands.QUERY_ENTRY;
    } else if (cmd.equals("back") || cmd.equals("b")) {
      return FinancePortfoliosClient.PositionCommands.BACK;
    } else if (cmd.equals("quit") || cmd.equals("q")) {
      return FinancePortfoliosClient.PositionCommands.QUIT;
    } else if (cmd.equals("transactions") || cmd.equals("t")) {
      return FinancePortfoliosClient.PositionCommands.TRANSACTIONS;
    } else {
      return FinancePortfoliosClient.PositionCommands.HELP;
    }
  }

  /**
   * Processes commands entered at the transaction menu.
   *
   * @param cmd user entered command
   * @return type of command
   */
  private static TransactionCommands processTransactionMenuCommand(String cmd) {
    if (cmd.equals("list") || cmd.equals("l")) {
      return FinancePortfoliosClient.TransactionCommands.QUERY_FEED;
    } else if (cmd.equals("show") || cmd.equals("s")) {
      return FinancePortfoliosClient.TransactionCommands.QUERY_ENTRY;
    } else if (cmd.equals("create") || cmd.equals("c")) {
      return FinancePortfoliosClient.TransactionCommands.CREATE;
    } else if (cmd.equals("update") || cmd.equals("u")) {
      return FinancePortfoliosClient.TransactionCommands.UPDATE;
    } else if (cmd.equals("delete") || cmd.equals("d")) {
      return FinancePortfoliosClient.TransactionCommands.DELETE;
    } else if (cmd.equals("back") || cmd.equals("b")) {
      return FinancePortfoliosClient.TransactionCommands.BACK;
    } else if (cmd.equals("quit") || cmd.equals("q")) {
      return FinancePortfoliosClient.TransactionCommands.QUIT;
    } else {
      return FinancePortfoliosClient.TransactionCommands.HELP;
    }
  }

  /**
   * Portfolio menu.
   *
   * @param sc Scanner to read user input from the command line.
   * @throws IOException If there is a problem communicating with the server.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void portfolioMenu(FinanceService service, Scanner sc)
      throws IOException, ServiceException {
    printPortfolioMenuHelp();
    while (true) {
      String requestUrl = baseUrl + PORTFOLIO_FEED_URL_SUFFIX;
      switch(processPortfolioMenuCommand(sc.nextLine().toLowerCase())) {
        case QUERY_FEED:
          System.out.print("Include returns in query response? (y/n) ");
          String includeReturns = sc.nextLine();
          if (includeReturns.toLowerCase().equals("y") ||
              includeReturns.toLowerCase().equals("yes")) {
            requestUrl += "?returns=true";
          } else if (includeReturns.toLowerCase().equals("n") ||
                     includeReturns.toLowerCase().equals("no")) {
            requestUrl += "?returns=false";
          }
          System.out.print("Inline positions in feed? (y/n) ");
          String inlinePositions = sc.nextLine();
          if (inlinePositions.toLowerCase().equals("y") ||
              inlinePositions.toLowerCase().equals("yes")) {
            requestUrl += "&positions=true";
          } else if (inlinePositions.toLowerCase().equals("n") ||
                     inlinePositions.toLowerCase().equals("no")) {
            requestUrl += "&positions=false";
          }
          queryPortfolioFeed(service, requestUrl);
          break;
        case QUERY_ENTRY:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          requestUrl += "/" + portfolioIdProperty;
          queryPortfolioEntry(service, requestUrl);
          break;
        case CREATE:
          System.out.println("Enter portfolio name");
          String portfolioName = sc.nextLine();
          System.out.println("Enter currency code");
          String currencyCode = sc.nextLine();
          PortfolioEntry entry = FinanceUtilities.makePortfolioEntry(portfolioName, currencyCode);
          insertPortfolioEntry(service, requestUrl, entry);
          break;
        case UPDATE:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          requestUrl += "/" + portfolioIdProperty;
          System.out.println("Enter new portfolio name");
          portfolioName = sc.nextLine();
          System.out.println("Enter new currency code");
          currencyCode = sc.nextLine();
          entry = FinanceUtilities.makePortfolioEntry(portfolioName, currencyCode);
          updatePortfolioEntry(service, requestUrl, entry);
          break;
        case DELETE:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          requestUrl += "/" + portfolioIdProperty;
          deletePortfolioEntry(service, requestUrl);
          break;
        case BACK:
          return;
        case POSITIONS:
          positionMenu(service, sc);
          break;
        case QUIT:
          System.exit(0);
        case HELP:
          printPortfolioMenuHelp();
          break;
        default:
          printPortfolioMenuHelp();
      }
    }
  }

  /**
   * Position menu.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param sc Scanner to read user input from the command line.
   * @throws IOException If there is a problem communicating with the server.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void positionMenu(FinanceService service, Scanner sc)
      throws IOException, ServiceException {
    printPositionMenuHelp();
    while (true) {
      String requestUrl = baseUrl + PORTFOLIO_FEED_URL_SUFFIX + "/";
      switch(processPositionMenuCommand(sc.nextLine().toLowerCase())) {
        case QUERY_FEED:
          System.out.print("Enter portfolio ID: ");
          portfolioIdProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX;
          System.out.print("Include returns in query response? (y/n) ");
          String includeReturns = sc.nextLine();
          if (includeReturns.toLowerCase().equals("y") ||
              includeReturns.toLowerCase().equals("yes")) {
            requestUrl += "?returns=true";
          } else if (includeReturns.toLowerCase().equals("n") ||
                     includeReturns.toLowerCase().equals("no")) {
            requestUrl += "?returns=false";
          }
          System.out.print("Inline transactions in feed? (y/n) ");
          String inlinePositions = sc.nextLine();
          if (inlinePositions.toLowerCase().equals("y") ||
              inlinePositions.toLowerCase().equals("yes")) {
            requestUrl += "&transactions=true";
          } else if (inlinePositions.toLowerCase().equals("n") ||
                     inlinePositions.toLowerCase().equals("no")) {
            requestUrl += "&transactions=false";
          }
          queryPositionFeed(service, requestUrl);
          break;
        case QUERY_ENTRY:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          System.out.println("Enter ticker (<exchange>:<ticker>)");
          tickerProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX + "/" + tickerProperty;
          queryPositionEntry(service, requestUrl);
          break;
        case BACK:
          return;
        case TRANSACTIONS:
          transactionMenu(service, sc);
          break;
        case QUIT:
          System.exit(0);
        case HELP:
          printPositionMenuHelp();
          break;
        default:
          printPositionMenuHelp();
      }
    }
  }

  /**
   * Transaction menu.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param sc Scanner to read user input from the command line.
   * @throws IOException If there is a problem communicating with the server.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void transactionMenu(FinanceService service, Scanner sc)
      throws IOException, ServiceException {
    printTransactionMenuHelp();
    while (true) {
      String requestUrl = baseUrl + PORTFOLIO_FEED_URL_SUFFIX + "/";
      switch(processTransactionMenuCommand(sc.nextLine().toLowerCase())) {
        case QUERY_FEED:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          System.out.println("Enter ticker (<exchange>:<symbol>)");
          tickerProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX + "/" + tickerProperty +
              TRANSACTION_FEED_URL_SUFFIX;
          queryTransactionFeed(service, requestUrl);
          break;
        case QUERY_ENTRY:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          System.out.println("Enter ticker (<exchange>:<ticker>)");
          tickerProperty = sc.nextLine();
          System.out.println("Enter transaction ID");
          transactionIdProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX + "/" + tickerProperty +
              TRANSACTION_FEED_URL_SUFFIX + "/" + transactionIdProperty;
          queryTransactionEntry(service, requestUrl);
          break;
        case CREATE:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          System.out.println("Enter ticker (<exchange>:<ticker>) ");
          tickerProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX + "/" + tickerProperty +
              TRANSACTION_FEED_URL_SUFFIX;
          System.out.print("Enter transaction type (Buy, Sell, Sell Short, Buy to Cover): ");
          String type = sc.nextLine();
          System.out.print("Enter transaction date (yyyy-mm-dd or blank): ");
          String date = sc.nextLine();
          System.out.print("Enter number of shares (optional, e.g. 100.0): ");
          String shares = sc.nextLine();
          System.out.print("Enter price (optional, e.g. 141.14): ");
          String price = sc.nextLine();
          System.out.print("Enter commission (optional, e.g. 20.0): ");
          String commission = sc.nextLine();
          System.out.print("Enter currency (optional, e.g. USD, EUR, JPY): ");
          String currency = sc.nextLine();
          System.out.print("Enter any notes: ");
          String notes = sc.nextLine();
          TransactionEntry entry = FinanceUtilities.makeTransactionEntry(
              type, date, shares, price, commission, currency, notes);
          insertTransactionEntry(service, requestUrl, entry);
          break;
        case UPDATE:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          System.out.println("Enter ticker (<exchange>:<ticker>) ");
          tickerProperty = sc.nextLine();
          System.out.println("Enter transaction ID");
          transactionIdProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX + "/" + tickerProperty +
              TRANSACTION_FEED_URL_SUFFIX + "/" + transactionIdProperty;
          System.out.print("Enter transaction type (Buy, Sell, Sell Short, Buy to Cover): ");
          type = sc.nextLine();
          System.out.print("Enter transaction date (yyyy-mm-dd or blank): ");
          date = sc.nextLine();
          System.out.print("Enter number of shares (optional, e.g. 100.0): ");
          shares = sc.nextLine();
          System.out.print("Enter price (optional, e.g. 141.14): ");
          price = sc.nextLine();
          System.out.print("Enter commission (optional, e.g. 20.0): ");
          commission = sc.nextLine();
          System.out.print("Enter currency (optional, e.g. USD, EUR, JPY): ");
          currency = sc.nextLine();
          System.out.print("Enter any notes: ");
          notes = sc.nextLine();
          entry = FinanceUtilities.makeTransactionEntry(
              type, date, shares, price, commission, currency, notes);
          updateTransactionEntry(service, requestUrl, entry);
          break;
        case DELETE:
          System.out.println("Enter portfolio ID");
          portfolioIdProperty = sc.nextLine();
          System.out.println("Enter ticker (<exchange>:<ticker>)");
          tickerProperty = sc.nextLine();
          System.out.println("Enter transaction ID");
          transactionIdProperty = sc.nextLine();
          requestUrl += portfolioIdProperty + POSITION_FEED_URL_SUFFIX + "/" + tickerProperty +
              TRANSACTION_FEED_URL_SUFFIX + "/" + transactionIdProperty;
          deleteTransactionEntry(service, requestUrl);
          break;
        case BACK:
          return;
        case QUIT:
          System.exit(0);
        case HELP:
          printTransactionMenuHelp();
          break;
        default:
          printTransactionMenuHelp();
      }
    }
  }

  /**
   * Prints the available commands at the main menu.
   */
  private static void printMainMenuHelp() {
    System.out.println("<command> (<abbreviated command>) : <description>");
    System.out.println("-------------------------------------------------");
    System.out.println("portfolios (pr)  : portfolio commands");
    System.out.println("positions (ps)   : position commands");
    System.out.println("transactions (t) : transaction commands");
    System.out.println("help (h)         : help");
    System.out.println("quit (q)         : quit");
    System.out.println("");
  }

  /**
   * Prints the available commands at the portfolio menu.
   */
  private static void printPortfolioMenuHelp() {
    System.out.println("<command> (<abbreviated command>) : <description>");
    System.out.println("-------------------------------------------------");
    System.out.println("list (l)      : list portfolios");
    System.out.println("show (s)      : show portfolio");
    System.out.println("create (c)    : create new portfolio");
    System.out.println("update (u)    : update portfolio name or currency");
    System.out.println("delete (d)    : delete portfolio");
    System.out.println("back (b)      : go back to previous menu");
    System.out.println("positions (p) : go to the positions menu");
    System.out.println("help (h)      : help");
    System.out.println("quit (q)      : quit");
    System.out.println("");
  }

  /**
   * Prints the available commands at the position menu.
   */
  private static void printPositionMenuHelp() {
    System.out.println("<command> (<abbreviated command>) : <description>");
    System.out.println("-------------------------------------------------");
    System.out.println("list (l)           : list positions");
    System.out.println("show (s)           : show position");
    System.out.println("back (b)           : go back to previous menu");
    System.out.println("transactions (t)   : go to the transactions menu");
    System.out.println("help (h)           : help");
    System.out.println("quit (q)           : quit");
    System.out.println("");
  }

  /**
   * Prints the available commands at the transaction menu.
   */
  private static void printTransactionMenuHelp() {
    System.out.println("<command> (<abbreviated command>) : <description>");
    System.out.println("-------------------------------------------------");
    System.out.println("list (l)   : list transactions");
    System.out.println("show (s)   : show transaction");
    System.out.println("create (c) : create new transaction");
    System.out.println("update (u) : update transaction details");
    System.out.println("delete (d) : delete transaction");
    System.out.println("back (b)   : go back to previous menu");
    System.out.println("help (h)   : help");
    System.out.println("quit (q)   : quit");
    System.out.println("");
  }

  /**
   * Prints detailed information regarding a Generic Feed
   *
   * @param feed The feed of interest
   */
  private static void printBasicFeedDetails(BaseFeed feed){
    System.out.println("\tFeed is " + (feed.getCanPost() ? "writable!" : "read-only!"));
    System.out.println("\tNumber of entries: " + feed.getTotalResults());
    System.out.println("\tStart Index: " + feed.getStartIndex());
    System.out.println("\tEnd Index: " + feed.getItemsPerPage());
    System.out.println("\tFeed URI: " + (feed.getSelfLink() == null ?
                                         "<none>" : feed.getSelfLink().getHref()) + "\n");
    System.out.println("\tFeed Title: " + feed.getTitle().getPlainText());
    System.out.println("\tAtom ID: " + feed.getId());
    System.out.println("\tLast updated: " + feed.getUpdated());
    System.out.println("\tFeed Categories:");
    Iterator it = feed.getCategories().iterator();
    while (it.hasNext()) {
      System.out.println("\t\t" + it.next().toString());
    }
    System.out.println("\tLinks:");
    if (feed.getLinks().size() == 0) {
      System.out.println("\t\t<No links, sorry!>");
    }
    for (int i = 0; i < feed.getLinks().size(); i++) {
      System.out.println("\t\t" + feed.getLinks().get(i).getHref());
    }
    System.out.println("\t" + "HTML Link: " + feed.getHtmlLink().getHref());
  }

  /**
   * Prints detailed information regarding a Generic Entry
   *
   * @param entry The entry of interest
   **/
  private static void printBasicEntryDetails(BaseEntry entry){
    System.out.println("\tTitle: " + entry.getTitle().getPlainText());
    System.out.println("\tAtom ID: " + entry.getId());
    System.out.println("\tLast updated: " + entry.getUpdated());
    System.out.println("\tEntry Categories:");
    Iterator it = entry.getCategories().iterator();
    while (it.hasNext()) {
      System.out.println("\t\t" + it.next().toString());
    }
    System.out.println("\tLinks:");
    if (entry.getLinks().size() == 0) {
      System.out.println("\t\t<No links, sorry!>");
    }
    for (int i = 0; i < entry.getLinks().size(); i++) {
      System.out.println("\t\t" + ((Link) (entry.getLinks().get(i))).getHref());
    }
  }

  /**
   * Prints detailed contents for a portfolio (i.e. a Portfolio Feed entry)
   *
   * @param portfolioEntry The portfolio entry of interest
   */
  private static void printPortfolioEntry(PortfolioEntry portfolioEntry) {
    System.out.println("\nPortfolio Entry\n---------------");
    printBasicEntryDetails(portfolioEntry);
    System.out.println("\tFeed Link: " + portfolioEntry.getFeedLink().getHref());
    if (portfolioEntry.getFeedLink().getFeed() == null) {
      System.out.println("\tNo inlined feed.");
    } else {
      System.out.println("********** Beginning of inline feed ***************");
      printBasicFeedDetails(portfolioEntry.getFeedLink().getFeed());
      PositionFeed inlinedFeed = portfolioEntry.getFeedLink().getFeed();
      printBasicFeedDetails(inlinedFeed);
      for (int i = 0; i < inlinedFeed.getEntries().size(); i++) {
        PositionEntry positionEntry = inlinedFeed.getEntries().get(i);
        printPositionEntry(positionEntry);
      }
      System.out.println("************* End of inlined feed *****************");
    }
    PortfolioData portfolioData = portfolioEntry.getPortfolioData();
    System.out.println("\tPortfolio Data:");
    System.out.println("\t\tCurrency is " + portfolioData.getCurrencyCode());
    System.out.printf("\t\tPercent Gain is %.2f%%\n", portfolioData.getGainPercentage() * 100.0);
    System.out.println("\t\tReturns:");
    System.out.printf("\t\t\tOne week: %.2f%%\n", portfolioData.getReturn1w() * 100.0);
    System.out.printf("\t\t\tFour weeks: %.2f%%\n", portfolioData.getReturn4w() * 100.0);
    System.out.printf("\t\t\tThree months: %.2f%%\n", portfolioData.getReturn3m() * 100.0);
    System.out.printf("\t\t\tYear-to-date: %.2f%%\n", portfolioData.getReturnYTD() * 100.0);
    System.out.printf("\t\t\tOne year: %.2f%%\n", portfolioData.getReturn1y() * 100.0);
    System.out.printf("\t\t\tThree years: %.2f%%\n", portfolioData.getReturn3y() * 100.0);
    System.out.printf("\t\t\tFive years: %.2f%%\n", portfolioData.getReturn5y() * 100.0);
    System.out.printf("\t\t\tOverall: %.2f%%\n", portfolioData.getReturnOverall() * 100.0);
    if (portfolioData.getCostBasis() == null) {
      System.out.println("\t\tCost Basis not specified");
    } else {
      for (int i = 0; i < portfolioData.getCostBasis().getMoney().size(); i++) {
        Money m = portfolioData.getCostBasis().getMoney().get(i);
        System.out.printf("\t\tThis portfolio cost %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (portfolioData.getDaysGain() == null) {
      System.out.println("\t\tDay's Gain not specified");
    } else {
      for (int i = 0; i < portfolioData.getDaysGain().getMoney().size(); i++) {
        Money m = portfolioData.getDaysGain().getMoney().get(i);
        System.out.printf("\t\tThis portfolio made %.2f %s today.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (portfolioData.getGain() == null) {
      System.out.println("\t\tTotal Gain not specified");
    } else {
      for (int i = 0; i < portfolioData.getGain().getMoney().size(); i++) {
        Money m = portfolioData.getGain().getMoney().get(i);
        System.out.printf("\t\tThis portfolio has a total gain of %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (portfolioData.getMarketValue() == null) {
      System.out.println("\t\tMarket Value not specified");
    } else {
      for (int i = 0; i < portfolioData.getMarketValue().getMoney().size(); i++) {
        Money m = portfolioData.getMarketValue().getMoney().get(i);
        System.out.printf("\t\tThis portfolio is worth %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
  }

  /**
   * Prints detailed contents for a position (i.e. a Position Feed entry)
   *
   * @param positionEntry The position entry of interest
   */
  private static void printPositionEntry(PositionEntry positionEntry) {
    System.out.println("\nPosition Entry\n--------------");
    printBasicEntryDetails(positionEntry);
    System.out.println("\tFeed Link: " + positionEntry.getFeedLink().getHref());
    if (positionEntry.getFeedLink().getFeed() == null) {
      System.out.println("\tNo inlined feed.");
    } else {
      System.out.println("********** Beginning of inline feed ***************");
      printBasicFeedDetails(positionEntry.getFeedLink().getFeed());
      TransactionFeed inlinedFeed = positionEntry.getFeedLink().getFeed();
      printBasicFeedDetails(inlinedFeed);
      for (int i = 0; i < inlinedFeed.getEntries().size(); i++) {
        TransactionEntry transactionEntry = inlinedFeed.getEntries().get(i);
        printTransactionEntry(transactionEntry);
      }
      System.out.println("************* End of inlined feed *****************");
    }
    System.out.println("\tTicker:");
    System.out.println("\t\tExchange: " + positionEntry.getSymbol().getExchange());
    System.out.println("\t\tSymbol: " + positionEntry.getSymbol().getSymbol());
    System.out.println("\t\tFull Name: " + positionEntry.getSymbol().getFullName());
    PositionData positionData = positionEntry.getPositionData();
    System.out.println("\tPosition Data:");
    System.out.printf("\t\tShare count: %.2f\n", positionData.getShares());
    System.out.printf("\t\tPercent Gain is %.2f%%\n", positionData.getGainPercentage() * 100.0);
    System.out.println("\t\tReturns:");
    System.out.printf("\t\t\tOne week: %.2f%%\n", positionData.getReturn1w() * 100.0);
    System.out.printf("\t\t\tFour weeks: %.2f%%\n", positionData.getReturn4w() * 100.0);
    System.out.printf("\t\t\tThree months: %.2f%%\n", positionData.getReturn3m() * 100.0);
    System.out.printf("\t\t\tYear-to-date: %.2f%%\n", positionData.getReturnYTD() * 100.0);
    System.out.printf("\t\t\tOne year: %.2f%%\n", positionData.getReturn1y() * 100.0);
    System.out.printf("\t\t\tThree years: %.2f%%\n", positionData.getReturn3y() * 100.0);
    System.out.printf("\t\t\tFive years: %.2f%%\n", positionData.getReturn5y() * 100.0);
    System.out.printf("\t\t\tOverall: %.2f%%\n", positionData.getReturnOverall() * 100.0);
    if (positionData.getCostBasis() == null) {
      System.out.println("\t\tCost Basis not specified");
    } else {
      for (int i = 0; i < positionData.getCostBasis().getMoney().size(); i++) {
        Money m = positionData.getCostBasis().getMoney().get(i);
        System.out.printf("\t\tThis position cost %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (positionData.getDaysGain() == null) {
      System.out.println("\t\tDay's Gain not specified");
    } else {
      for (int i = 0; i < positionData.getDaysGain().getMoney().size(); i++) {
        Money m = positionData.getDaysGain().getMoney().get(i);
        System.out.printf("\t\tThis position made %.2f %s today.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (positionData.getGain() == null) {
      System.out.println("\t\tTotal Gain not specified");
    } else {
      for (int i = 0; i < positionData.getGain().getMoney().size(); i++) {
        Money m = positionData.getGain().getMoney().get(i);
        System.out.printf("\t\tThis position has a total gain of %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (positionData.getMarketValue() == null) {
      System.out.println("\t\tMarket Value not specified");
    } else {
      for (int i = 0; i < positionData.getMarketValue().getMoney().size(); i++) {
        Money m = positionData.getMarketValue().getMoney().get(i);
        System.out.printf("\t\tThis position is worth %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
  }

  /**
   * Prints detailed contents for a transaction (i.e. a Transaction Feed entry)
   *
   * @param transactionEntry The transaction entry of interest
   */
  private static void printTransactionEntry(TransactionEntry transactionEntry) {
    System.out.println("\nTransaction Entry\n-----------------");
    printBasicEntryDetails(transactionEntry);
    TransactionData transactionData = transactionEntry.getTransactionData();
    System.out.println("\tTransaction Data:");
    System.out.println("\t\tType: " +
                       (transactionData.getType() == null ? "no type" : transactionData.getType()));
    System.out.println("\t\tDate: " +
                       (transactionData.getDate() == null ? "no date" : transactionData.getDate()));
    System.out.printf("\t\tShares: %.2f\n", transactionData.getShares());
    if (transactionData.getPrice() == null) {
      System.out.println("\t\tPrice not specified");
    } else {
      for (int i = 0; i < transactionData.getPrice().getMoney().size(); i++) {
        Money m = transactionData.getPrice().getMoney().get(i);
        System.out.printf("\t\tThis transaction had a unit price of %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    if (transactionData.getCommission() == null) {
      System.out.println("\t\tCommission not specified");
    } else {
      for (int i = 0; i < transactionData.getCommission().getMoney().size(); i++) {
        Money m = transactionData.getCommission().getMoney().get(i);
        System.out.printf("\t\tThis transaction had a commission of %.2f %s.\n",
                          m.getAmount(), m.getCurrencyCode());
      }
    }
    System.out.println("\t\tNotes: " +
                       (null == transactionData.getNotes() ? "none" : transactionData.getNotes()));
  }

  /**
   * Queries a portfolio feed and prints feed and entry details.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param feedUrl resource URL for the feed, including GData query parameters
   *   e.g. http://finance.google.com/finance/feeds/default/portfolios
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void queryPortfolioFeed(FinanceService service, String feedUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Requesting Feed at location: " + feedUrl);
    PortfolioFeed portfolioFeed = service.getFeed(new URL(feedUrl), PortfolioFeed.class);
    System.out.println("\nPortfolio Feed\n==============");
    printBasicFeedDetails(portfolioFeed);
    for (int i = 0; i < portfolioFeed.getEntries().size(); i++) {
      PortfolioEntry portfolioEntry = portfolioFeed.getEntries().get(i);
      printPortfolioEntry(portfolioEntry);
    }
  }

  /**
   * Queries a portfolio entry and prints entry details.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl resource URL for the entry
   *   e.g. http://finance.google.com/finance/feeds/default/portfolios/1
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void queryPortfolioEntry(FinanceService service, String entryUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Requesting Entry at location: " + entryUrl);
    PortfolioEntry portfolioEntry = service.getEntry(new URL(entryUrl), PortfolioEntry.class);
    printPortfolioEntry(portfolioEntry);
  }

  /**
   * Inserts a portfolio entry into a feed and prints the new entry.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param feedUrl the POST URI associated with the target feed
   *   e.g. http://finance.google.com/finance/feeds/default/portfolios
   * @param entry the new entry to insert into the feed
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void insertPortfolioEntry(FinanceService service,
                                           String feedUrl,
                                           PortfolioEntry entry)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Inserting Entry at location: " + feedUrl);
    PortfolioEntry insertedEntry = service.insert(new URL(feedUrl), entry);
    printPortfolioEntry(insertedEntry);
  }

  /**
   * Updates a portfolio entry in a feed and prints the updated entry.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl the edit URL associated with the entry
   *   e.g. http://finance.google.com/finance/feeds/default/portfolios/1
   * @param entry the modified Entry to be written to the server
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void updatePortfolioEntry(FinanceService service,
                                           String entryUrl,
                                           PortfolioEntry entry)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Updating Entry at location: " + entryUrl);
    PortfolioEntry updatedEntry = service.update(new URL(entryUrl), entry);
    printPortfolioEntry(updatedEntry);
  }

  /**
   * Deletes a portfolio entry in a feed.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl the edit URL associated with the entry
   *   e.g. http://finance.google.com/finance/feeds/default/portfolios/1
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void deletePortfolioEntry(FinanceService service, String entryUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Deleting Entry at location: " + entryUrl);
    service.delete(new URL(entryUrl));
    System.out.println("Delete Successful");
  }

  /**
   * Queries a position feed and prints feed and entry details.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param feedUrl resource URL for the feed, including GData query parameters
   *   e.g. http://finance.google.com/finance/feeds/default/
                portfolios/1/positions?returns=true
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void queryPositionFeed(FinanceService service, String feedUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Requesting Feed at location " + feedUrl);
    PositionFeed positionFeed = service.getFeed(new URL(feedUrl), PositionFeed.class);
    System.out.println("\nPosition Feed\n=============");
    printBasicFeedDetails(positionFeed);
    for (int i = 0; i < positionFeed.getEntries().size(); i++) {
      PositionEntry positionEntry = positionFeed.getEntries().get(i);
      printPositionEntry(positionEntry);
    }
  }

  /**
   * Queries a positon entry and prints entry details.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl resource URL for the entry
   *   e.g. http://finance.google.com/finance/feeds/default/
   *            portfolios/1/positions/NYSE:IBM
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void queryPositionEntry(FinanceService service, String entryUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Requesting Entry at location " + entryUrl);
    PositionEntry positionEntry = service.getEntry(new URL(entryUrl), PositionEntry.class);
    printPositionEntry(positionEntry);
  }

  /**
   * Queries a transaction feed and prints feed and entry details.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param feedUrl resource URL for the feed, including GData query parameters
   *   e.g. http://finance.google.com/finance/feeds/default/
   *            portfolios/1/positions/NYSE:IBM/transactions
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void queryTransactionFeed(FinanceService service, String feedUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Requesting Feed at location " + feedUrl);
    TransactionFeed transactionFeed = service.getFeed(new URL(feedUrl), TransactionFeed.class);
    System.out.println("\nTransaction Feed\n================");
    printBasicFeedDetails(transactionFeed);
    for (int i = 0; i < transactionFeed.getEntries().size(); i++) {
      TransactionEntry transactionEntry = transactionFeed.getEntries().get(i);
      printTransactionEntry(transactionEntry);
    }
  }

  /**
   * Queries a transaction entry and prints entry details.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl resource URL for the entry
   *   e.g. http://finance.google.com/finance/feeds/default/
   *            portfolios/1/positions/NYSE:IBM/transactions/1
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void queryTransactionEntry(FinanceService service, String entryUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Requesting Entry at location: " + entryUrl);
    TransactionEntry transactionEntry =
        service.getEntry(new URL(entryUrl), TransactionEntry.class);
    printTransactionEntry(transactionEntry);
  }

  /**
   * Inserts a transaction entry into a feed and prints the new entry.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param feedUrl the POST URI associated with the target feed
   *   e.g. http://finance.google.com/finance/feeds/default/
   *            portfolios/1/positions/NYSE:IBM/transactions
   * @param entry the new entry to insert into the feed
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void insertTransactionEntry(FinanceService service,
                                             String feedUrl,
                                             TransactionEntry entry)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Inserting Entry at location: " + feedUrl);
    TransactionEntry insertedEntry = service.insert(new URL(feedUrl), entry);
    printTransactionEntry(insertedEntry);
  }

  /**
   * Updates a transaction entry in a feed and prints the updated entry.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl the edit URL associated with the entry
   *   e.g. http://finance.google.com/finance/feeds/default/
   *            portfolios/1/positions/NYSE:IBM/transactions/1
   * @param entry the modified Entry to be written to the server
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void updateTransactionEntry(FinanceService service,
                                             String entryUrl,
                                             TransactionEntry entry)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Updating Entry at location: " + entryUrl);
    TransactionEntry updatedEntry = service.update(new URL(entryUrl), entry);
    printTransactionEntry(updatedEntry);
  }

  /**
   * Deletes a transaction entry in a feed.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param entryUrl the edit URL associated with the entry
   *   e.g. http://finance.google.com/finance/feeds/default/
   *            portfolios/1/positions/NYSE:IBM/transactions/1
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void deleteTransactionEntry(FinanceService service, String entryUrl)
      throws IOException, MalformedURLException, ServiceException {
    System.out.println("Deleting Entry at location: " + entryUrl);
    service.delete(new URL(entryUrl));
    System.out.println("Delete Successful");
  }

  /**
   * Authenticates the user with the Google server after reading in the user
   * email and password.
   *
   * @param service authenticated client connection to a Finance GData service
   * @param userID Finance portfolio user ID (e.g. "bob@gmail.com")
   * @param userPassword Finance portfolio user password (e.g. "Bobs$tocks")
   * @return login success or failure
   */
  private static Boolean loginUser(FinanceService service, String userID, String userPassword) {
    try {
      service.setUserCredentials(userID, userPassword);
    } catch (AuthenticationException e) {
      System.err.println("Invalid Credentials!");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private static void printUsage() {
    System.out.println("  Usage:");
    System.out.println("    FinancePortfoliosClient <server> <account> <password>");
    System.out.println("  Example:");
    System.out.println("    FinancePortfoliosClient " +
                       "http://finance.google.com user@gmail.com password");
  }

  /**
   * Main menu for the Sample Google Finance Portfolios Client.
   *
   * @param args server, user email, and user password in that order.
   */
  public static void main(String[] args) {
    // Google Finance GData service.
    FinanceService service = new FinanceService("Google-PortfoliosDemo-1.0");
    Scanner sc = new Scanner(System.in);
    System.out.println("Sample Google Finance Portfolios Client");
    if (args.length != 1 && args.length != 3) {
      printUsage();
      System.exit(0);
    }
    // Set username and password from command-line arguments if they were passed
    // in. Otherwise prompt the user to login.
    if (args.length == 3) {
      userEmail = args[1];
      userPassword = args[2];
    } else {
      System.out.print("Enter user ID: ");
      userEmail = sc.nextLine();
      System.out.print("Enter user password: ");
      userPassword = sc.nextLine();
    }
    if (!loginUser(service, userEmail, userPassword)) {
      printUsage();
      System.exit(0);
    }
    server = args[0];
    baseUrl = server + basePath + "default";
    printMainMenuHelp();
    try {
      while (true) {
        switch(processMainMenuCommand(sc.nextLine().toLowerCase())) {
          case PORTFOLIOS:
            portfolioMenu(service, sc);
            break;
          case POSITIONS:
            positionMenu(service, sc);
            break;
          case TRANSACTIONS:
            transactionMenu(service, sc);
            break;
          case QUIT:
            System.exit(0);
          case HELP:
            printMainMenuHelp();
            break;
          default:
            printMainMenuHelp();
        }
      }
    } catch (IOException e) {
      // Communication error.
      System.err.println("There was a problem communicating with the service.");
      e.printStackTrace();
    } catch (ServiceException e) {
      // Server side error.
      System.err.println("The server had a problem handling your request.");
      e.printStackTrace();
    }
  }

  /** This class is only used for its static methods */
  private FinancePortfoliosClient() {
  }
}
