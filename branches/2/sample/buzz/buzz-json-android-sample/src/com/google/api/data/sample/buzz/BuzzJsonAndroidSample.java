/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.buzz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.api.client.apache.ApacheHttpTransport;
import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCallbackUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpTransport;
import com.google.api.data.sample.buzz.model.BuzzActivity;
import com.google.api.data.sample.buzz.model.BuzzActivityFeed;
import com.google.api.data.sample.buzz.model.BuzzObject;
import com.google.api.data.sample.buzz.model.BuzzUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample for Google Buzz API of an Android application that shows the end-user
 * their Buzz activities and allows them to post a new Buzz activity. It also
 * demonstrates a good way to do OAuth 1.0a authentication.
 * <p>
 * To enable logging of HTTP requests/responses, change {@link BuzzUrl#DEBUG} to
 * true and run this command: {@code adb shell setprop log.tag.HttpTransport
 * DEBUG}.
 * </p>
 * 
 * @author Yaniv Inbar
 */
public class BuzzJsonAndroidSample extends Activity {

  /** Buzz OAuth scope with write permissions. */
  private static final String SCOPE = "https://www.googleapis.com/auth/buzz";

  private static final String APP_NAME = "buzzsample";

  private static final int CONTEXT_DELETE = 0;

  private static HttpTransport transport;

  private static boolean isTemporary;

  private static OAuthCredentialsResponse credentials;

  private List<BuzzActivity> activities = new ArrayList<BuzzActivity>();

  private static OAuthHmacSigner createOAuthSigner() {
    OAuthHmacSigner result = new OAuthHmacSigner();
    if (credentials != null) {
      result.tokenSharedSecret = credentials.tokenSecret;
    }
    result.clientSharedSecret = "anonymous";
    return result;
  }

  private static OAuthParameters createOAuthParameters() {
    OAuthParameters authorizer = new OAuthParameters();
    authorizer.consumerKey = "anonymous";
    authorizer.signer = createOAuthSigner();
    authorizer.token = credentials.token;
    return authorizer;
  }

  public BuzzJsonAndroidSample() {
    HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);
    transport = GoogleTransport.create();
    GoogleHeaders headers = (GoogleHeaders) transport.defaultHeaders;
    headers.setApplicationName("google-buzzsample-1.0");
    if (BuzzUrl.DEBUG) {
      Logger.getLogger("com.google.api.client").setLevel(Level.ALL);
    }
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      boolean isViewAction = Intent.ACTION_VIEW.equals(getIntent().getAction());
      if (!isViewAction && (isTemporary || credentials == null)) {
        GoogleOAuthGetTemporaryToken temporaryToken =
            new GoogleOAuthGetTemporaryToken();
        temporaryToken.signer = createOAuthSigner();
        temporaryToken.consumerKey = "anonymous";
        temporaryToken.scope = SCOPE;
        temporaryToken.displayName = APP_NAME;
        temporaryToken.callback = "buzz-sample:///";
        isTemporary = true;
        credentials = temporaryToken.execute();
        OAuthAuthorizeTemporaryTokenUrl authorizeUrl =
            new OAuthAuthorizeTemporaryTokenUrl(
                "https://www.google.com/buzz/api/auth/OAuthAuthorizeToken");
        authorizeUrl.set("scope", SCOPE);
        authorizeUrl.set("domain", "anonymous");
        authorizeUrl.set("xoauth_displayname", APP_NAME);
        authorizeUrl.temporaryToken = credentials.token;
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(authorizeUrl.build()));
        startActivity(webIntent);
      } else {
        if (isViewAction) {
          Uri uri = this.getIntent().getData();
          OAuthCallbackUrl callbackUrl = new OAuthCallbackUrl(uri.toString());
          GoogleOAuthGetAccessToken accessToken =
              new GoogleOAuthGetAccessToken();
          accessToken.temporaryToken = callbackUrl.token;
          accessToken.verifier = callbackUrl.verifier;
          accessToken.signer = createOAuthSigner();
          accessToken.consumerKey = "anonymous";
          isTemporary = false;
          credentials = accessToken.execute();
          createOAuthParameters().signRequestsUsingAuthorizationHeader(
              transport);
        }
        authenticated();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, CONTEXT_DELETE, 0, getString(R.string.delete));
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
      case CONTEXT_DELETE:
        BuzzActivity activity = this.activities.get(info.position);
        try {
          activity.delete(transport);
          refreshActivities();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return true;
      default:
        return super.onContextItemSelected(item);
    }
  }

  private void authenticated() {
    // set up transport
    transport.addParser(new JsonCParser());
    // set up view
    setContentView(R.layout.main);
    Button postButton = (Button) findViewById(R.id.postButton);
    postButton.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        BuzzActivity activity = new BuzzActivity();
        activity.object = new BuzzObject();
        EditText postText = (EditText) findViewById(R.id.postText);
        activity.object.content = postText.getText().toString();
        try {
          activity.post(transport);
          refreshActivities();
        } catch (IOException e) {
          e.printStackTrace();
        }
        postText.setText("");
      }
    });
    refreshActivities();
  }

  private void refreshActivities() {
    String[] activityContents =
        new String[] {getString(R.string.no_activities)};
    try {
      BuzzActivityFeed feed = BuzzActivityFeed.list(transport);
      List<BuzzActivity> activities = this.activities;
      activities.clear();
      if (feed.activities != null) {
        int size = feed.activities.size();
        activityContents = new String[size];
        for (int i = 0; i < size; i++) {
          BuzzActivity activity = feed.activities.get(i);
          activities.add(activity);
          activityContents[i] = activity.object.content;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    ListView activitiesListView = (ListView) findViewById(R.id.activities);
    activitiesListView.setAdapter(new ArrayAdapter<String>(this,
        R.layout.textview, R.id.textView, activityContents));
    registerForContextMenu(activitiesListView);
  }
}
