// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.android;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.google.api.data.client.http.LowLevelHttpTransportInterface;
import com.google.api.data.client.http.apache.ApacheGData;

public class AndroidGData {

  // TODO: take advantage of android.net.Uri?

  // TODO: android.util.Log for logging?

  public static final LowLevelHttpTransportInterface HTTP_TRANSPORT =
      ApacheGData.HTTP_TRANSPORT;

  public static Account[] getGoogleAccounts(AccountManager manager) {
    return manager.getAccountsByType("com.google");
  }

}
