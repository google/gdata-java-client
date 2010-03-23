// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import com.google.api.data.client.generator.linewrap.LineWrapper;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Generate {

  public static void main(String[] args) throws IOException {
    System.out.println("GData Generator");
    if (args.length < 2) {
      System.err.println("Expected arguments: dataDirectory gdataLibraryDirectory");
      System.exit(1);
    }
    SortedSet<Client> clients = readClients(args[0]);
    // display clients
    System.out.println();
    System.out.println(clients.size() + " clients:");
    for (Client client : clients) {
      System.out.print(client.name + " (" + client.id + "))");
      if (client.versions.size() == 1) {
        System.out.println(" version " + client.versions.first().id);
      } else {
        System.out.print(" versions ");
        boolean first = true;
        for (Version version : client.versions) {
          if (first) {
            first = false;
          } else {
            System.out.print(", ");
          }
          System.out.print(version.id);
        }
        System.out.println();
      }
    }
    // compute file generators
    List<FileGenerator> fileGenerators = new ArrayList<FileGenerator>();
    fileGenerators.add(new AntBuildFileGenerator(clients));
    for (Client client : clients) {
      for (Version version : client.versions) {
        fileGenerators.add(new MainJavaFileGenerator(version));
        fileGenerators.add(new MainPackageFileGenerator(version));
        fileGenerators.add(new AtomPackageFileGenerator(version));
        fileGenerators.add(new AtomJavaFileGenerator(version));
      }
    }
    File gdataRootDir = getDirectory(args[1]);
    System.out.println();
    int size = fileGenerators.size();
    System.out.println(size + " generated files:");
    for (int i = 0; i < size; i++) {
      FileGenerator fileGenerator = fileGenerators.get(i);
      String outputFilePath = fileGenerator.getOutputFilePath();
      File mainFile = new File(gdataRootDir, outputFilePath);
      boolean exists = mainFile.exists();
      boolean isGenerated = fileGenerator.isGenerated();
      if (isGenerated || exists) {
        System.out.print("[" + (i + 1) + " of " + size + "] " + outputFilePath);
      }
      if (isGenerated) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter stringPrintWriter = new PrintWriter(stringWriter);
        fileGenerator.generate(stringPrintWriter);
        String content = stringWriter.toString();
        LineWrapper lineWrapper = fileGenerator.getLineWrapper();
        if (lineWrapper != null) {
          content = lineWrapper.compute(content);
        }
        mainFile.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(mainFile);
        fileWriter.write(content);
        fileWriter.close();
        if (exists) {
          System.out.println(" (updated)");
        } else {
          System.out.println(" (added)");
        }
      } else if (exists) {
        mainFile.delete();
        System.out.println(" (deleted)");
      }
    }
  }

  private static SortedSet<Client> readClients(String dataDirectoryPath) throws IOException {
    File dataDirectory = getDirectory(dataDirectoryPath);
    SortedSet<Client> result = new TreeSet<Client>();
    JsonFactory factory = new JsonFactory();
    for (File file : dataDirectory.listFiles()) {
      if (!file.getName().endsWith(".json")) {
        continue;
      }
      try {
        JsonParser parser = factory.createJsonParser(file);
        Client client = new Client();
        parser.nextToken();
        client.parse(parser);
        parser.close();
        result.add(client);
      } catch (RuntimeException e) {
        throw new RuntimeException("problem parsing " + file.getCanonicalPath(), e);
      }
    }
    return result;
  }

  private Generate() {
  }

  private static File getDirectory(String path) {
    File directory = new File(path);
    if (!directory.isDirectory()) {
      System.err.println("not a directory: " + path);
      System.exit(1);
    }
    return directory;
  }
}
