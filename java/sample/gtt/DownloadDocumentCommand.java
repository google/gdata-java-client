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


package sample.gtt;

import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ServiceException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Downloads a translated document.
 *
 * 
 */
public class DownloadDocumentCommand implements Command {

  public static final DownloadDocumentCommand INSTANCE
      = new DownloadDocumentCommand();

  /**
   * This is a singleton.
   */
  private DownloadDocumentCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String id = parser.getValue("id");
    URL feedUrl = FeedUris.getDocumentDownloadFeedUrl(id);

    String targetFile = parser.getValue("file");

    System.out.print("Downloading document with id :" + id + " ....");
    System.out.flush();

    MediaContent mc = new MediaContent();
    mc.setUri(feedUrl.toString());
    MediaSource ms = service.getMedia(mc);

    InputStream inStream = null;
    FileOutputStream outStream = null;
    try {
      inStream = ms.getInputStream();
      outStream = new FileOutputStream(targetFile);

      int c;
      while ((c = inStream.read()) != -1) {
        outStream.write(c);
      }
    } finally {
      if (inStream != null) {
        inStream.close();
      }
      if (outStream != null) {
        outStream.flush();
        outStream.close();
      }
    }

    System.out.print("....done. Saved translation to '" + targetFile + "' .");
  }

  public String helpString() {
    return "Downloads a translation document."
        + "\n\t--id <id>\t; Id of the document to download"
        + "\n\t--file <filepath>\t; path where downloaded doc is to be stored";
  }
}
