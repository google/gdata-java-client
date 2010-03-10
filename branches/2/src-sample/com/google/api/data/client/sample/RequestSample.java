package com.google.api.data.client.sample;

import com.google.api.data.client.v2.jsonc.JsoncEntity;
import com.google.api.data.client.v2.request.RequestFactory;
import com.google.api.data.client.v2.request.ServiceDocument;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RequestSample {

  private static RequestFactory apis;

  public static void main(String[] args) throws Exception {
    init();
    listAlbumVariations();
    xdremel();
  }

  static void init() throws Exception {
    // Defines the REST service to use for all requests
    apis = new RequestFactory.Builder()
        .restService(getServiceDocument("photos", "xdremel"))
        .with("projection", "api")
        .with("alt", "jsonc")
        .build();
  }

  /**
   * Three different ways to get the same set of albums
   */
  public static void listAlbumVariations() {
    // Untyped
    JsoncEntity e = apis.query("photos.user")
        .with("username", "gdatajavaclienttest")
        .with("fields", "items(title)")
        .returning(JsoncEntity.class)
        .execute();
    System.out.println(e);
    System.out.println("----------------");   
  }
  
  public static void xdremel() {
    JsoncEntity e = apis.query("xdremel.table")
        .with("tableName", "abc.test")
        .returning(JsoncEntity.class)
        .execute();
    System.out.println(e);
    System.out.println("----------------");
  }


  public static class UserDefinedAlbum {
    public String name;
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
            RequestSample.class.getResourceAsStream("services.json")));
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
