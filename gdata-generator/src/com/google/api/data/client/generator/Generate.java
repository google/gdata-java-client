// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import com.google.api.data.client.generator.linewrap.LineWrapper;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Generate {

  File gdataRootDir;

  public static void main(String[] args) throws IOException {
    System.out.println("GData Generator");
    if (args.length < 2) {
      System.err.println("Expected arguments: dataDirectory gdataLibraryDirectory");
      System.exit(1);
    }
    SortedSet<Client> clients = readClients(args[0]);
    // display clients
    System.out.println();
    System.out.println(clients.size() + " API description(s):");
    for (Client client : clients) {
      System.out.print(client.name + " (" + client.id + ")");
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
    Generate generate = new Generate();
    generate.gdataRootDir = getDirectory(args[1]);
    int size = 0;
    List<FileComputer> fileComputers = new ArrayList<FileComputer>();
    System.out.println();
    System.out.println("Computing " + fileGenerators.size() + " file(s):");
    for (FileGenerator fileGenerator : fileGenerators) {
      FileComputer fileComputer = generate.new FileComputer(fileGenerator);
      fileComputers.add(fileComputer);
      fileComputer.compute();
      System.out.print('.');
      if (fileComputer.status != FileStatus.UNCHANGED) {
        size++;
      }
    }
    System.out.println();
    System.out.println();
    if (size != 0) {
      System.out.println(size + " update(s):");
      int index = 0;
      for (FileComputer fileComputer : fileComputers) {
        if (fileComputer.status != FileStatus.UNCHANGED) {
          index++;
          System.out.println(fileComputer.outputFilePath + " ("
              + fileComputer.status.toString().toLowerCase() + ")");
        }
      }
    } else {
      System.out.println("All files up to date.");
    }
  }

  enum FileStatus {
    UNCHANGED, ADDED, UPDATED, DELETED
  }

  class FileComputer {
    private final FileGenerator fileGenerator;
    FileStatus status = FileStatus.UNCHANGED;
    final String outputFilePath;

    FileComputer(FileGenerator fileGenerator) {
      this.fileGenerator = fileGenerator;
      outputFilePath = fileGenerator.getOutputFilePath();
    }

    void compute() throws IOException {
      File file = new File(gdataRootDir, outputFilePath);
      boolean exists = file.exists();
      boolean isGenerated = fileGenerator.isGenerated();
      if (isGenerated) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter stringPrintWriter = new PrintWriter(stringWriter);
        fileGenerator.generate(stringPrintWriter);
        String content = stringWriter.toString();
        LineWrapper lineWrapper = fileGenerator.getLineWrapper();
        if (lineWrapper != null) {
          content = lineWrapper.compute(content);
        }
        if (exists) {
          String currentContent = readFile(file);
          if (currentContent.equals(content)) {
            return;
          }
        }
        file.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
        if (exists) {
          status = FileStatus.UPDATED;
        } else {
          status = FileStatus.ADDED;
        }
      } else if (exists) {
        file.delete();
        status = FileStatus.DELETED;
      }
    }
  }

  private Generate() {
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

  private static File getDirectory(String path) {
    File directory = new File(path);
    if (!directory.isDirectory()) {
      System.err.println("not a directory: " + path);
      System.exit(1);
    }
    return directory;
  }

  private static String readFile(File file) throws IOException {
    InputStream content = new FileInputStream(file);
    try {
      int length = (int) file.length();
      byte[] buffer = new byte[length];
      content.read(buffer);
      return new String(buffer, 0, length);
    } finally {
      content.close();
    }
  }
}
