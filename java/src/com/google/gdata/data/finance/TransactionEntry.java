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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

/**
 * Describes an entry in a feed of Finance transactions.
 *
 * 
 */
@Kind.Term(TransactionEntry.KIND)
public class TransactionEntry extends BaseEntry<TransactionEntry> {

  /**
   * Transaction kind term value.
   */
  public static final String KIND = FinanceNamespace.GF_PREFIX + "transaction";

  /**
   * Transaction kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public TransactionEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public TransactionEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(TransactionEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(TransactionEntry.class,
        TransactionData.getDefaultDescription(true, false));
    new TransactionData().declareExtensions(extProfile);
  }

  /**
   * Returns the data for the transaction.
   *
   * @return data for the transaction
   */
  public TransactionData getTransactionData() {
    return getExtension(TransactionData.class);
  }

  /**
   * Sets the data for the transaction.
   *
   * @param transactionData data for the transaction or <code>null</code> to
   *     reset
   */
  public void setTransactionData(TransactionData transactionData) {
    if (transactionData == null) {
      removeExtension(TransactionData.class);
    } else {
      setExtension(transactionData);
    }
  }

  /**
   * Returns whether it has the data for the transaction.
   *
   * @return whether it has the data for the transaction
   */
  public boolean hasTransactionData() {
    return hasExtension(TransactionData.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{TransactionEntry " + super.toString() + "}";
  }

}

