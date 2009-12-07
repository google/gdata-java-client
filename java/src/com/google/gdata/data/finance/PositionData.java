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


package com.google.gdata.data.finance;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;

/**
 * Data for the position.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = FinanceNamespace.GF_ALIAS,
    nsUri = FinanceNamespace.GF,
    localName = PositionData.XML_NAME)
public class PositionData extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "positionData";

  /** XML "gainPercentage" attribute name */
  private static final String GAINPERCENTAGE = "gainPercentage";

  /** XML "return1w" attribute name */
  private static final String RETURN1W = "return1w";

  /** XML "return1y" attribute name */
  private static final String RETURN1Y = "return1y";

  /** XML "return3m" attribute name */
  private static final String RETURN3M = "return3m";

  /** XML "return3y" attribute name */
  private static final String RETURN3Y = "return3y";

  /** XML "return4w" attribute name */
  private static final String RETURN4W = "return4w";

  /** XML "return5y" attribute name */
  private static final String RETURN5Y = "return5y";

  /** XML "returnOverall" attribute name */
  private static final String RETURNOVERALL = "returnOverall";

  /** XML "returnYTD" attribute name */
  private static final String RETURNYTD = "returnYTD";

  /** XML "shares" attribute name */
  private static final String SHARES = "shares";

  /** Percentage gain */
  private Double gainPercentage = null;

  /** 1 week return (percentage) */
  private Double return1w = null;

  /** 1 year return (percentage) */
  private Double return1y = null;

  /** 3 month return (percentage) */
  private Double return3m = null;

  /** 3 year return (percentage) */
  private Double return3y = null;

  /** 4 week return (percentage) */
  private Double return4w = null;

  /** 5 year return (percentage) */
  private Double return5y = null;

  /** Overall return (percentage) */
  private Double returnOverall = null;

  /** Year-to-date return (percentage) */
  private Double returnYTD = null;

  /** Number of shares belonging to the position */
  private Double shares = null;

  /**
   * Default mutable constructor.
   */
  public PositionData() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param gainPercentage percentage gain.
   * @param return1w 1 week return (percentage).
   * @param return1y 1 year return (percentage).
   * @param return3m 3 month return (percentage).
   * @param return3y 3 year return (percentage).
   * @param return4w 4 week return (percentage).
   * @param return5y 5 year return (percentage).
   * @param returnOverall overall return (percentage).
   * @param returnYTD Year-to-date return (percentage).
   * @param shares number of shares belonging to the position.
   */
  public PositionData(Double gainPercentage, Double return1w, Double return1y,
      Double return3m, Double return3y, Double return4w, Double return5y,
      Double returnOverall, Double returnYTD, Double shares) {
    super();
    setGainPercentage(gainPercentage);
    setReturn1w(return1w);
    setReturn1y(return1y);
    setReturn3m(return3m);
    setReturn3y(return3y);
    setReturn4w(return4w);
    setReturn5y(return5y);
    setReturnOverall(returnOverall);
    setReturnYTD(returnYTD);
    setShares(shares);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(PositionData.class)) {
      return;
    }
    extProfile.declare(PositionData.class, CostBasis.class);
    new CostBasis().declareExtensions(extProfile);
    extProfile.declare(PositionData.class, DaysGain.class);
    new DaysGain().declareExtensions(extProfile);
    extProfile.declare(PositionData.class, Gain.class);
    new Gain().declareExtensions(extProfile);
    extProfile.declare(PositionData.class, MarketValue.class);
    new MarketValue().declareExtensions(extProfile);
  }

  /**
   * Returns the cost basis of the position in the portfolio default currency
   * (and exchange currency if it differs).
   *
   * @return cost basis of the position in the portfolio default currency (and
   *     exchange currency if it differs)
   */
  public CostBasis getCostBasis() {
    return getExtension(CostBasis.class);
  }

  /**
   * Sets the cost basis of the position in the portfolio default currency (and
   * exchange currency if it differs).
   *
   * @param costBasis cost basis of the position in the portfolio default
   *     currency (and exchange currency if it differs) or <code>null</code> to
   *     reset
   */
  public void setCostBasis(CostBasis costBasis) {
    if (costBasis == null) {
      removeExtension(CostBasis.class);
    } else {
      setExtension(costBasis);
    }
  }

  /**
   * Returns whether it has the cost basis of the position in the portfolio
   * default currency (and exchange currency if it differs).
   *
   * @return whether it has the cost basis of the position in the portfolio
   *     default currency (and exchange currency if it differs)
   */
  public boolean hasCostBasis() {
    return hasExtension(CostBasis.class);
  }

  /**
   * Returns the today's gain for the position in the portfolio default currency
   * (and exchange currency if it differs).
   *
   * @return today's gain for the position in the portfolio default currency
   *     (and exchange currency if it differs)
   */
  public DaysGain getDaysGain() {
    return getExtension(DaysGain.class);
  }

  /**
   * Sets the today's gain for the position in the portfolio default currency
   * (and exchange currency if it differs).
   *
   * @param daysGain today's gain for the position in the portfolio default
   *     currency (and exchange currency if it differs) or <code>null</code> to
   *     reset
   */
  public void setDaysGain(DaysGain daysGain) {
    if (daysGain == null) {
      removeExtension(DaysGain.class);
    } else {
      setExtension(daysGain);
    }
  }

  /**
   * Returns whether it has the today's gain for the position in the portfolio
   * default currency (and exchange currency if it differs).
   *
   * @return whether it has the today's gain for the position in the portfolio
   *     default currency (and exchange currency if it differs)
   */
  public boolean hasDaysGain() {
    return hasExtension(DaysGain.class);
  }

  /**
   * Returns the gain for the position in the portfolio default currency (and
   * exchange currency if it differs).
   *
   * @return gain for the position in the portfolio default currency (and
   *     exchange currency if it differs)
   */
  public Gain getGain() {
    return getExtension(Gain.class);
  }

  /**
   * Sets the gain for the position in the portfolio default currency (and
   * exchange currency if it differs).
   *
   * @param gain gain for the position in the portfolio default currency (and
   *     exchange currency if it differs) or <code>null</code> to reset
   */
  public void setGain(Gain gain) {
    if (gain == null) {
      removeExtension(Gain.class);
    } else {
      setExtension(gain);
    }
  }

  /**
   * Returns whether it has the gain for the position in the portfolio default
   * currency (and exchange currency if it differs).
   *
   * @return whether it has the gain for the position in the portfolio default
   *     currency (and exchange currency if it differs)
   */
  public boolean hasGain() {
    return hasExtension(Gain.class);
  }

  /**
   * Returns the percentage gain.
   *
   * @return percentage gain
   */
  public Double getGainPercentage() {
    return gainPercentage;
  }

  /**
   * Sets the percentage gain.
   *
   * @param gainPercentage percentage gain or <code>null</code> to reset
   */
  public void setGainPercentage(Double gainPercentage) {
    throwExceptionIfImmutable();
    this.gainPercentage = gainPercentage;
  }

  /**
   * Returns whether it has the percentage gain.
   *
   * @return whether it has the percentage gain
   */
  public boolean hasGainPercentage() {
    return getGainPercentage() != null;
  }

  /**
   * Returns the market value of the position in the portfolio default currency
   * (and exchange currency if it differs).
   *
   * @return market value of the position in the portfolio default currency (and
   *     exchange currency if it differs)
   */
  public MarketValue getMarketValue() {
    return getExtension(MarketValue.class);
  }

  /**
   * Sets the market value of the position in the portfolio default currency
   * (and exchange currency if it differs).
   *
   * @param marketValue market value of the position in the portfolio default
   *     currency (and exchange currency if it differs) or <code>null</code> to
   *     reset
   */
  public void setMarketValue(MarketValue marketValue) {
    if (marketValue == null) {
      removeExtension(MarketValue.class);
    } else {
      setExtension(marketValue);
    }
  }

  /**
   * Returns whether it has the market value of the position in the portfolio
   * default currency (and exchange currency if it differs).
   *
   * @return whether it has the market value of the position in the portfolio
   *     default currency (and exchange currency if it differs)
   */
  public boolean hasMarketValue() {
    return hasExtension(MarketValue.class);
  }

  /**
   * Returns the 1 week return (percentage).
   *
   * @return 1 week return (percentage)
   */
  public Double getReturn1w() {
    return return1w;
  }

  /**
   * Sets the 1 week return (percentage).
   *
   * @param return1w 1 week return (percentage) or <code>null</code> to reset
   */
  public void setReturn1w(Double return1w) {
    throwExceptionIfImmutable();
    this.return1w = return1w;
  }

  /**
   * Returns whether it has the 1 week return (percentage).
   *
   * @return whether it has the 1 week return (percentage)
   */
  public boolean hasReturn1w() {
    return getReturn1w() != null;
  }

  /**
   * Returns the 1 year return (percentage).
   *
   * @return 1 year return (percentage)
   */
  public Double getReturn1y() {
    return return1y;
  }

  /**
   * Sets the 1 year return (percentage).
   *
   * @param return1y 1 year return (percentage) or <code>null</code> to reset
   */
  public void setReturn1y(Double return1y) {
    throwExceptionIfImmutable();
    this.return1y = return1y;
  }

  /**
   * Returns whether it has the 1 year return (percentage).
   *
   * @return whether it has the 1 year return (percentage)
   */
  public boolean hasReturn1y() {
    return getReturn1y() != null;
  }

  /**
   * Returns the 3 month return (percentage).
   *
   * @return 3 month return (percentage)
   */
  public Double getReturn3m() {
    return return3m;
  }

  /**
   * Sets the 3 month return (percentage).
   *
   * @param return3m 3 month return (percentage) or <code>null</code> to reset
   */
  public void setReturn3m(Double return3m) {
    throwExceptionIfImmutable();
    this.return3m = return3m;
  }

  /**
   * Returns whether it has the 3 month return (percentage).
   *
   * @return whether it has the 3 month return (percentage)
   */
  public boolean hasReturn3m() {
    return getReturn3m() != null;
  }

  /**
   * Returns the 3 year return (percentage).
   *
   * @return 3 year return (percentage)
   */
  public Double getReturn3y() {
    return return3y;
  }

  /**
   * Sets the 3 year return (percentage).
   *
   * @param return3y 3 year return (percentage) or <code>null</code> to reset
   */
  public void setReturn3y(Double return3y) {
    throwExceptionIfImmutable();
    this.return3y = return3y;
  }

  /**
   * Returns whether it has the 3 year return (percentage).
   *
   * @return whether it has the 3 year return (percentage)
   */
  public boolean hasReturn3y() {
    return getReturn3y() != null;
  }

  /**
   * Returns the 4 week return (percentage).
   *
   * @return 4 week return (percentage)
   */
  public Double getReturn4w() {
    return return4w;
  }

  /**
   * Sets the 4 week return (percentage).
   *
   * @param return4w 4 week return (percentage) or <code>null</code> to reset
   */
  public void setReturn4w(Double return4w) {
    throwExceptionIfImmutable();
    this.return4w = return4w;
  }

  /**
   * Returns whether it has the 4 week return (percentage).
   *
   * @return whether it has the 4 week return (percentage)
   */
  public boolean hasReturn4w() {
    return getReturn4w() != null;
  }

  /**
   * Returns the 5 year return (percentage).
   *
   * @return 5 year return (percentage)
   */
  public Double getReturn5y() {
    return return5y;
  }

  /**
   * Sets the 5 year return (percentage).
   *
   * @param return5y 5 year return (percentage) or <code>null</code> to reset
   */
  public void setReturn5y(Double return5y) {
    throwExceptionIfImmutable();
    this.return5y = return5y;
  }

  /**
   * Returns whether it has the 5 year return (percentage).
   *
   * @return whether it has the 5 year return (percentage)
   */
  public boolean hasReturn5y() {
    return getReturn5y() != null;
  }

  /**
   * Returns the overall return (percentage).
   *
   * @return overall return (percentage)
   */
  public Double getReturnOverall() {
    return returnOverall;
  }

  /**
   * Sets the overall return (percentage).
   *
   * @param returnOverall overall return (percentage) or <code>null</code> to
   *     reset
   */
  public void setReturnOverall(Double returnOverall) {
    throwExceptionIfImmutable();
    this.returnOverall = returnOverall;
  }

  /**
   * Returns whether it has the overall return (percentage).
   *
   * @return whether it has the overall return (percentage)
   */
  public boolean hasReturnOverall() {
    return getReturnOverall() != null;
  }

  /**
   * Returns the Year-to-date return (percentage).
   *
   * @return Year-to-date return (percentage)
   */
  public Double getReturnYTD() {
    return returnYTD;
  }

  /**
   * Sets the Year-to-date return (percentage).
   *
   * @param returnYTD Year-to-date return (percentage) or <code>null</code> to
   *     reset
   */
  public void setReturnYTD(Double returnYTD) {
    throwExceptionIfImmutable();
    this.returnYTD = returnYTD;
  }

  /**
   * Returns whether it has the Year-to-date return (percentage).
   *
   * @return whether it has the Year-to-date return (percentage)
   */
  public boolean hasReturnYTD() {
    return getReturnYTD() != null;
  }

  /**
   * Returns the number of shares belonging to the position.
   *
   * @return number of shares belonging to the position
   */
  public Double getShares() {
    return shares;
  }

  /**
   * Sets the number of shares belonging to the position.
   *
   * @param shares number of shares belonging to the position or
   *     <code>null</code> to reset
   */
  public void setShares(Double shares) {
    throwExceptionIfImmutable();
    this.shares = shares;
  }

  /**
   * Returns whether it has the number of shares belonging to the position.
   *
   * @return whether it has the number of shares belonging to the position
   */
  public boolean hasShares() {
    return getShares() != null;
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(PositionData.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(GAINPERCENTAGE, gainPercentage);
    generator.put(RETURN1W, return1w);
    generator.put(RETURN1Y, return1y);
    generator.put(RETURN3M, return3m);
    generator.put(RETURN3Y, return3y);
    generator.put(RETURN4W, return4w);
    generator.put(RETURN5Y, return5y);
    generator.put(RETURNOVERALL, returnOverall);
    generator.put(RETURNYTD, returnYTD);
    generator.put(SHARES, shares);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    gainPercentage = helper.consumeDouble(GAINPERCENTAGE, false);
    return1w = helper.consumeDouble(RETURN1W, false);
    return1y = helper.consumeDouble(RETURN1Y, false);
    return3m = helper.consumeDouble(RETURN3M, false);
    return3y = helper.consumeDouble(RETURN3Y, false);
    return4w = helper.consumeDouble(RETURN4W, false);
    return5y = helper.consumeDouble(RETURN5Y, false);
    returnOverall = helper.consumeDouble(RETURNOVERALL, false);
    returnYTD = helper.consumeDouble(RETURNYTD, false);
    shares = helper.consumeDouble(SHARES, false);
  }

  @Override
  public String toString() {
    return "{PositionData gainPercentage=" + gainPercentage + " return1w=" +
        return1w + " return1y=" + return1y + " return3m=" + return3m +
        " return3y=" + return3y + " return4w=" + return4w + " return5y=" +
        return5y + " returnOverall=" + returnOverall + " returnYTD=" + returnYTD
        + " shares=" + shares + "}";
  }

}

