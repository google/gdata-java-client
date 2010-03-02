// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.android;

import com.google.api.data.client.v2.GDataClientFactory;
import com.google.api.data.client.v2.apache.ApacheGData;

import android.accounts.Account;
import android.accounts.AccountManager;

public class AndroidGData {

  // TODO: take advantage of android.net.Uri?

  // TODO: android.util.Log for logging?
  
  public static final GDataClientFactory CLIENT_FACTORY =
      ApacheGData.CLIENT_FACTORY;

  public static Account[] getGoogleAccounts(AccountManager manager) {
    return manager.getAccountsByType("com.google");
  }

}
