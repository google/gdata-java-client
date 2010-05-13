package com.google.api.data.sample.picasa;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.api.client.apache.ApacheHttpTransport;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.util.DateTime;
import com.google.api.client.xml.atom.AtomParser;
import com.google.api.data.picasa.v2.Picasa;
import com.google.api.data.picasa.v2.PicasaPath;
import com.google.api.data.picasa.v2.atom.PicasaAtom;
import com.google.api.data.sample.picasa.model.AlbumEntry;
import com.google.api.data.sample.picasa.model.UserFeed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample for Picasa Web Albums Data API using the Atom wire format. It shows
 * how to authenticate, get albums, add a new album, update it, and delete it.
 * <p>
 * It also demonstrates how to upload a photo to the "Drop Box" album. To see
 * this example in action, take a picture, click "Share", and select
 * "Picasa Basic Android Sample".
 * </p>
 * <p>
 * To enable logging of HTTP requests/responses, run this command: {@code adb
 * shell setprop log.tag.HttpTransport DEBUG}. Then press-and-hold an album, and
 * enable "Logging".
 * </p>
 * 
 * @author Yaniv Inbar
 */
public class PicasaBasicAtomAndroidSample extends ListActivity {


  enum AuthType {
    ACCOUNT_MANAGER, CLIENT_LOGIN
  }

  private static AuthType AUTH_TYPE = AuthType.ACCOUNT_MANAGER;

  private static final String TAG = "PicasaBasicAtomAndroidSample";

  private static final int MENU_ADD = 0;

  private static final int MENU_ACCOUNTS = 1;

  private static final int CONTEXT_EDIT = 0;

  private static final int CONTEXT_DELETE = 1;

  private static final int CONTEXT_LOGGING = 2;

  private static final int REQUEST_AUTHENTICATE = 0;

  private static final int REQUEST_ADD_ACCOUNT = 1;

  private static final String PREF = "MyPrefs";

  private static final int DIALOG_ACCOUNTS = 0;

  private GoogleTransport transport =
      new GoogleTransport("google-picasaandroidsample-1.0");

  private String authToken;

  private String postLink;

  private final List<AlbumEntry> albums = new ArrayList<AlbumEntry>();

  public PicasaBasicAtomAndroidSample() {
    GoogleTransport transport = this.transport;
    transport.setVersionHeader(Picasa.VERSION);
    AtomParser parser = new AtomParser();
    parser.namespaceDictionary = PicasaAtom.NAMESPACE_DICTIONARY;
    transport.addParser(parser);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    setLogging(settings.getBoolean("logging", false));
    getListView().setTextFilterEnabled(true);
    registerForContextMenu(getListView());
    gotAccount(false);
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case DIALOG_ACCOUNTS:
        switch (AUTH_TYPE) {
          case ACCOUNT_MANAGER:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select a Google account");
            final AccountManager manager = AccountManager.get(this);
            final Account[] accounts = manager.getAccountsByType("com.google");
            final int size = accounts.length;
            String[] names = new String[size + 1];
            for (int i = 0; i < size; i++) {
              names[i] = accounts[i].name;
            }
            names[size] = "New Account";
            builder.setItems(names, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (which == size) {
                  addAccount(manager);
                } else {
                  gotAccount(manager, accounts[which]);
                }
              }
            });
            return builder.create();
          case CLIENT_LOGIN:
            final Dialog clientLoginDialog = new Dialog(this);
            clientLoginDialog.setContentView(R.layout.clientlogin);
            clientLoginDialog.setTitle("Sign in with your Google Account");
            Button signInButton =
                (Button) clientLoginDialog.findViewById(R.id.SignIn);
            final EditText password =
                (EditText) clientLoginDialog.findViewById(R.id.Password);
            password.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
            signInButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                clientLoginDialog.dismiss();
                ClientLogin authenticator = new ClientLogin();
                authenticator.authTokenType = Picasa.AUTH_TOKEN_TYPE;
                EditText username =
                    (EditText) clientLoginDialog.findViewById(R.id.Email);
                authenticator.username = username.getText().toString();
                authenticator.password = password.getText().toString();
                try {
                  authenticator.authenticate().setAuthorizationHeader(
                      PicasaBasicAtomAndroidSample.this.transport);
                  authenticated();
                } catch (IOException e) {
                  handleException(e);
                }
              }
            });
            return clientLoginDialog;
        }
    }
    return null;
  }

  private void gotAccount(boolean tokenExpired) {
    switch (AUTH_TYPE) {
      case ACCOUNT_MANAGER:
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        String accountName = settings.getString("accountName", null);
        if (accountName != null) {
          AccountManager manager = AccountManager.get(this);
          Account[] accounts = manager.getAccountsByType("com.google");
          int size = accounts.length;
          for (int i = 0; i < size; i++) {
            Account account = accounts[i];
            if (accountName.equals(account.name)) {
              if (tokenExpired) {
                manager.invalidateAuthToken("com.google", this.authToken);
              }
              gotAccount(manager, account);
              return;
            }
          }
        }
        break;
    }
    showDialog(DIALOG_ACCOUNTS);
  }

  private void addAccount(AccountManager manager) {
    // TODO: test!
    try {
      Bundle bundle =
          manager.addAccount("google.com", Picasa.AUTH_TOKEN_TYPE, null, null,
              this, null, null).getResult();
      if (bundle.containsKey(AccountManager.KEY_INTENT)) {
        Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
        int flags = intent.getFlags();
        flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
        intent.setFlags(flags);
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT);
      } else {
        addAccountResult(bundle);
      }
    } catch (Exception e) {
      handleException(e);
    }
  }

  private void addAccountResult(Bundle bundle) {
    // TODO: test!
    String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
    String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("accountName", accountName);
    editor.commit();
    authenticatedClientLogin(authToken);
  }

  private void gotAccount(AccountManager manager, Account account) {
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("accountName", account.name);
    editor.commit();
    try {
      Bundle bundle =
          manager.getAuthToken(account, Picasa.AUTH_TOKEN_TYPE, true, null,
              null).getResult();
      if (bundle.containsKey(AccountManager.KEY_INTENT)) {
        Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
        int flags = intent.getFlags();
        flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
        intent.setFlags(flags);
        startActivityForResult(intent, REQUEST_AUTHENTICATE);
      } else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
        authenticatedClientLogin(bundle.getString(AccountManager.KEY_AUTHTOKEN));
      }
    } catch (Exception e) {
      handleException(e);
      return;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_AUTHENTICATE:
        if (resultCode == RESULT_OK) {
          gotAccount(false);
        } else {
          showDialog(DIALOG_ACCOUNTS);
        }
        break;
      case REQUEST_ADD_ACCOUNT:
        // TODO: test!
        if (resultCode == RESULT_OK) {
          addAccountResult(data.getExtras());
        } else {
          showDialog(DIALOG_ACCOUNTS);
        }
    }
  }

  private void authenticatedClientLogin(String authToken) {
    this.authToken = authToken;
    this.transport.setClientLoginToken(authToken);
    authenticated();
  }

  private void authenticated() {
    Intent intent = getIntent();
    if (Intent.ACTION_SEND.equals(intent.getAction())) {
      Bundle extras = intent.getExtras();
      if (extras.containsKey(Intent.EXTRA_STREAM)) {
        Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
        String scheme = uri.getScheme();
        if (scheme.equals("content")) {
          ContentResolver contentResolver = getContentResolver();
          Cursor cursor = contentResolver.query(uri, null, null, null, null);
          cursor.moveToFirst();
          String filePath =
              cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DATA));
          String fileName =
              cursor.getString(cursor
                  .getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
          PicasaPath path = PicasaPath.feed();
          path.user = "default";
          path.albumId = "default";
          GenericUrl feedUrl = new GenericUrl(path.build());
          boolean success = false;
          try {
            HttpRequest request = this.transport.buildPostRequest();
            request.url = feedUrl;
            GoogleHeaders.setSlug(request.headers, fileName);
            InputStreamContent content = new InputStreamContent();
            content.inputStream = contentResolver.openInputStream(uri);
            content.type = intent.getType();
            content.length =
                cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
            request.content = content;
            request.execute().ignore();
            success = true;
          } catch (IOException e) {
            handleException(e);
          }
          setListAdapter(new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, new String[] {success ? "OK"
                  : "ERROR"}));
        }
      }
    } else {
      executeRefreshAlbums();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_ADD, 0, "New album");
    menu.add(0, MENU_ACCOUNTS, 0, "Accounts");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case MENU_ADD:
        AlbumEntry album = new AlbumEntry();
        album.access = "private";
        album.title = "Album " + new DateTime(new Date());
        try {
          AlbumEntry.executeInsert(this.transport, album, this.postLink);
        } catch (IOException e) {
          handleException(e);
        }
        executeRefreshAlbums();
        return true;
      case MENU_ACCOUNTS:
        showDialog(DIALOG_ACCOUNTS);
        return true;
    }
    return false;
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, CONTEXT_EDIT, 0, "Update Title");
    menu.add(0, CONTEXT_DELETE, 0, "Delete");
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    boolean logging = settings.getBoolean("logging", false);
    menu.add(0, CONTEXT_LOGGING, 0, "Logging").setCheckable(true).setChecked(
        logging);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    AlbumEntry album = albums.get((int) info.id);
    GoogleTransport transport = this.transport;
    HttpRequest request;
    try {
      switch (item.getItemId()) {
        case CONTEXT_EDIT:
          AlbumEntry patchedAlbum = album.clone();
          patchedAlbum.title =
              album.title + " UPDATED " + new DateTime(new Date());
          patchedAlbum.executePatchRelativeToOriginal(this.transport, album);
          executeRefreshAlbums();
          return true;
        case CONTEXT_DELETE:
          album.executeDelete(this.transport);
          executeRefreshAlbums();
          return true;
        case CONTEXT_LOGGING:
          SharedPreferences settings = getSharedPreferences(PREF, 0);
          boolean logging = settings.getBoolean("logging", false);
          setLogging(!logging);
          return true;
        default:
          return super.onContextItemSelected(item);
      }
    } catch (IOException e) {
      handleException(e);
    }
    return false;
  }

  private void executeRefreshAlbums() {
    String[] albumNames;
    List<AlbumEntry> albums = this.albums;
    albums.clear();
    try {
      PicasaPath path = PicasaPath.feed();
      path.user = "default";
      String url = path.build();
      // page through results
      while (true) {
        UserFeed userFeed = UserFeed.executeGet(this.transport, url);
        this.postLink = userFeed.getPostLink();
        if (userFeed.albums != null) {
          albums.addAll(userFeed.albums);
        }
        String nextLink = userFeed.getNextLink();
        if (nextLink == null) {
          break;
        }
      }
      int numAlbums = albums.size();
      albumNames = new String[numAlbums];
      for (int i = 0; i < numAlbums; i++) {
        albumNames[i] = albums.get(i).title;
      }
    } catch (IOException e) {
      handleException(e);
      albumNames = new String[] {e.getMessage()};
      albums.clear();
    }
    setListAdapter(new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, albumNames));
  }

  private void setLogging(boolean logging) {
    Logger.getLogger("com.google.api.client").setLevel(
        logging ? Level.CONFIG : Level.OFF);
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    boolean currentSetting = settings.getBoolean("logging", false);
    if (currentSetting != logging) {
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("logging", logging);
      editor.commit();
    }
  }

  private void handleException(Exception e) {
    e.printStackTrace();
    if (e instanceof HttpResponseException) {
      int statusCode = ((HttpResponseException) e).response.statusCode;
      if (statusCode == 401 || statusCode == 403) {
        gotAccount(true);
      }
      return;
    }
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    if (settings.getBoolean("logging", false)) {
      if (e instanceof HttpResponseException) {
        try {
          Log.e(TAG, ((HttpResponseException) e).response.parseAsString());
        } catch (IOException parseException) {
          parseException.printStackTrace();
        }
      }
      Log.e(TAG, e.getMessage(), e);
    }
  }
}
