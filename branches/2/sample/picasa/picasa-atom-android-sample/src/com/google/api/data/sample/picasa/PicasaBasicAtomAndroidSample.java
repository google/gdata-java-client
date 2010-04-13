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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.api.client.DateTime;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.InputStreamHttpSerializer;
import com.google.api.client.http.UriEntity;
import com.google.api.client.http.android.AndroidGData;
import com.google.api.client.http.googleapis.GoogleHttp;
import com.google.api.client.http.googleapis.GoogleTransport;
import com.google.api.client.http.xml.atom.AtomHttpParser;
import com.google.api.data.picasa.v2.Picasa;
import com.google.api.data.picasa.v2.PicasaPath;
import com.google.api.data.picasa.v2.atom.PicasaAtom;
import com.google.api.data.sample.picasa.model.AlbumEntry;
import com.google.api.data.sample.picasa.model.UserFeed;

import java.io.IOException;
import java.io.InputStream;
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
 */
public class PicasaBasicAtomAndroidSample extends ListActivity {

  private static final int MENU_ADD = 0;

  private static final int MENU_ACCOUNTS = 1;

  private static final int CONTEXT_EDIT = 0;

  private static final int CONTEXT_DELETE = 1;

  private static final int CONTEXT_LOGGING = 2;

  private static final String PREF = "MyPrefs";

  private static final int DIALOG_ACCOUNTS = 0;

  private GoogleTransport transport;

  private String postLink;

  private List<AlbumEntry> albums = new ArrayList<AlbumEntry>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    String accountName = settings.getString("accountName", null);
    setLogging(settings.getBoolean("logging", false));
    if (accountName != null) {
      AccountManager manager = AccountManager.get(this);
      Account[] accounts = AndroidGData.getGoogleAccounts(manager);
      int size = accounts.length;
      for (int i = 0; i < size; i++) {
        Account account = accounts[i];
        if (accountName.equals(account.name)) {
          gotAccount(manager, account);
          return;
        }
      }
    }
    showDialog(DIALOG_ACCOUNTS);
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case DIALOG_ACCOUNTS:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Google account");
        final AccountManager manager = AccountManager.get(this);
        final Account[] accounts = AndroidGData.getGoogleAccounts(manager);
        int size = accounts.length;
        String[] names = new String[size];
        for (int i = 0; i < size; i++) {
          names[i] = accounts[i].name;
        }
        builder.setItems(names, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            gotAccount(manager, accounts[which]);
          }
        });
        return builder.create();
      default:
        return null;
    }
  }

  private void gotAccount(AccountManager manager, Account account) {
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("accountName", account.name);
    editor.commit();
    String authToken;
    try {
      authToken =
          manager.blockingGetAuthToken(account, Picasa.AUTH_TOKEN_TYPE, true);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), e.getMessage(),
          Toast.LENGTH_SHORT);
      return;
    }
    GoogleTransport transport =
        this.transport = new GoogleTransport("google-picasaandroidsample-1.0");
    transport.lowLevelHttpTransport = AndroidGData.HTTP_TRANSPORT;
    transport.setGDataVersionHeader(Picasa.VERSION);
    transport.setGoogleLoginAuthorizationHeader(authToken);
    AtomHttpParser parser = new AtomHttpParser();
    parser.namespaceDictionary = PicasaAtom.NAMESPACE_DICTIONARY;
    transport.setParser(parser);
    Intent intent = getIntent();
    if (Intent.ACTION_SEND.equals(intent.getAction())) {
      Bundle extras = intent.getExtras();
      if (extras.containsKey(Intent.EXTRA_STREAM)) {
        Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
        String scheme = uri.getScheme();
        if (scheme.equals("content")) {
          String mimeType = intent.getType();
          ContentResolver contentResolver = getContentResolver();
          Cursor cursor = contentResolver.query(uri, null, null, null, null);
          cursor.moveToFirst();
          String filePath =
              cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DATA));
          String fileName =
              cursor.getString(cursor
                  .getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
          long fileSize =
              cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
          PicasaPath path = PicasaPath.feed();
          path.user = "default";
          path.albumId = "default";
          UriEntity feedUri = new UriEntity(path.build());
          boolean success = false;
          try {
            InputStream stream = contentResolver.openInputStream(uri);
            HttpRequest request = transport.buildPostRequest(feedUri.build());
            GoogleHttp.setSlugHeader(request, fileName);
            request.setContent(new InputStreamHttpSerializer(stream, fileSize,
                mimeType, null));
            request.execute().ignore();
            success = true;
          } catch (IOException e) {
            e.printStackTrace();
          }
          setListAdapter(new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, new String[] {success ? "OK"
                  : "ERROR"}));
        }
      }
    } else {
      executeRefreshAlbums();
    }
    getListView().setTextFilterEnabled(true);
    registerForContextMenu(getListView());
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
          e.printStackTrace();
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
      e.printStackTrace();
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
      String uri = path.build();
      // page through results
      while (true) {
        UserFeed userFeed = UserFeed.executeGet(this.transport, uri);
        this.postLink = userFeed.getPostLink();
        albums.addAll(userFeed.albums);
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
      e.printStackTrace();
      albumNames = new String[] {e.getMessage()};
      albums.clear();
    }
    setListAdapter(new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, albumNames));
  }

  private void setLogging(boolean logging) {
    Logger.getLogger("com.google.api.client").setLevel(
        logging ? Level.ALL : Level.OFF);
    SharedPreferences settings = getSharedPreferences(PREF, 0);
    boolean currentSetting = settings.getBoolean("logging", false);
    if (currentSetting != logging) {
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("logging", logging);
      editor.commit();
    }
  }
}
