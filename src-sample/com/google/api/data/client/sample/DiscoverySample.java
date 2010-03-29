package com.google.api.data.client.sample;

import com.google.api.data.client.auth.Authorizer;
import com.google.api.data.client.auth.clientlogin.ClientLoginAuthenticator;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.v2.discovery.ApiRequestFactory;
import com.google.api.data.client.v2.discovery.ServiceDocument;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class DiscoverySample {

  private static ApiRequestFactory apis;
  
  public static class Item {
    public String etag;
  }
  
  public static class Album extends Item {
    public String title;
    public String id;
    public String access;
    public String description;
    public String kind;
    public Links links;
  }
  
  public static class Links {
    public String edit;
  }
  
  public static class Albums {
    public List<Album> items;
  }
  
  public static class Photo extends Item {
    public String title;
  }

  public static void main(String[] args) throws Exception {
    init();
    getAlbums();
    Album album = createAlbum();
    // updateAlbum();  //uncomment after partial + jsonc is supported
    deleteAlbum(album);
  }

  static void init() throws Exception {
    HttpTransport transport = new HttpTransport("sample_app");
    
    ClientLoginAuthenticator authenticator = new ClientLoginAuthenticator();
    authenticator.username = "gdata.java.client.test@gmail.com";
    authenticator.password = "testing";
    authenticator.httpTransport = transport;
    authenticator.authTokenType = "lh2";
    Authorizer authorizer = authenticator.getAuthorizer();
    
    // Defines the REST service to use for all requests
    apis = new ApiRequestFactory.Builder()
        .restService(getServiceDocument("photos"))
        .withAuth(authorizer)
        .with("projection", "api")
        .with("username", "gdata.java.client.test")
        .with("alt", "jsonc")
        .with("v", "2")
        .build();
  }

  /**
   * Three different ways to get the same set of albums
   */
  public static void getAlbums() {
    // Untyped
    Albums e = apis.query("photos.user")
        .returning(Albums.class)
        .execute();
    for (Album album : e.items) {
      System.out.println("id: " + album.id + 
          ", title: " + album.title + ", description: " + album.description);
    }
    System.out.println("----------------");   
  }
  
  public static Album createAlbum() {
    Album newAlbum = new Album();
    newAlbum.access = "private";
    newAlbum.title = "A new album";
    newAlbum.description = "My favorite photos";
    newAlbum.kind = "album";
    
    Album insertedAlbum = apis.insert("photos.user", newAlbum)
        .returning(Album.class)
        .execute();
    System.out.println("inserted: " + insertedAlbum.title);
    return insertedAlbum;
  }
  
  public static void updateAlbum() {
    Album updatedAlbum = new Album();
    updatedAlbum.title = "updated title";
    
    Album updated = apis.update("photos.user", updatedAlbum)
        .with("albumId", "5067564196745072737")
        .returning(Album.class)
        .execute();
    System.out.println("updated: " + updated.title);
  }
  
  public static void deleteAlbum(Album album) {
    apis.delete(album.links.edit)
        .with("etag", album.etag)
        .execute();
  }
  
  /**
   * HACK: Retrieves the service document for specified service.  The service
   * document can be lodaed from a local json configuration file or from a
   * service discovery uri.  A service document can contain definition for one
   * or more services at the same time.
   *
   * @param services
   * @return
   * @throws java.io.IOException
   */
  public static ServiceDocument getServiceDocument(String ... services)
      throws IOException {

    BufferedReader reader = new BufferedReader(
        new InputStreamReader(
            DiscoverySample.class.getResourceAsStream("discovery.json")));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(baos);
    String text;
    while ((text = reader.readLine()) != null) {
      writer.println(text);
    }
    writer.close();

    // Verify that the document has all requested services
    ServiceDocument serviceDoc = new ServiceDocument(baos.toString());
    for (String service : services) {
      if (!serviceDoc.hasService(service)) {
        throw new RuntimeException("Configuration error");
      }
    }

    return serviceDoc;
  }

  
}
