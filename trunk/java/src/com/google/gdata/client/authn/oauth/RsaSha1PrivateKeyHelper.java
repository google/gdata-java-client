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


package com.google.gdata.client.authn.oauth;

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * A collection of methods to load an RSA-SHA1 {@link java.security.PrivateKey}
 * object from various sources.  The key should be a Base-64 encoded private key
 * string conforming to the PKCS #8 standard.
 *
 * 
 */
public class RsaSha1PrivateKeyHelper {

  private RsaSha1PrivateKeyHelper() {
  }

  /**
   * Retrieves a {@link java.security.PrivateKey} from a file.
   *
   * @param filename The filename from which to load the private key.
   * @return A {@link java.security.PrivateKey} object.
   * @throws Base64DecoderException
   * @throws InvalidKeySpecException
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  public static PrivateKey getPrivateKeyFromFilename(String filename)
      throws Base64DecoderException, InvalidKeySpecException, IOException,
      NoSuchAlgorithmException {
    return getPrivateKey(new File(filename));
  }

  /**
   * Retrieves a {@link java.security.PrivateKey} from a file.
   *
   * @param file The {@link java.io.File} object from which to load the private
   *     key.
   * @return A {@link java.security.PrivateKey} object.
   * @throws Base64DecoderException
   * @throws InvalidKeySpecException
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  public static PrivateKey getPrivateKey(File file)
      throws Base64DecoderException, InvalidKeySpecException, IOException,
      NoSuchAlgorithmException {
    return getPrivateKey(new BufferedReader(new FileReader(file)));
  }

  /**
   * Retrieves a {@link java.security.PrivateKey} from a reader.
   *
   * @param privateKeyReader The {@link java.io.Reader} object from which to
   *     load the private key.
   * @return A {@link java.security.PrivateKey} object.
   * @throws Base64DecoderException
   * @throws InvalidKeySpecException
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  public static PrivateKey getPrivateKey(Reader privateKeyReader)
      throws Base64DecoderException, InvalidKeySpecException, IOException,
      NoSuchAlgorithmException {
    return getPrivateKey(readToString(privateKeyReader));
  }

  /**
   * Retrieves a {@link java.security.PrivateKey} from a string.
   *
   * @param privateKeyString The string from which to load the private key.
   * @return A {@link java.security.PrivateKey} object.
   * @throws Base64DecoderException
   * @throws InvalidKeySpecException
   * @throws NoSuchAlgorithmException
   */
  public static PrivateKey getPrivateKey(String privateKeyString)
      throws Base64DecoderException, InvalidKeySpecException,
      NoSuchAlgorithmException {
    // Strip off delimiters, if they exist.
    String begin = "-----BEGIN PRIVATE KEY-----";
    String end = "-----END PRIVATE KEY-----";
    if (privateKeyString.contains(begin) && privateKeyString.contains(end)) {
      privateKeyString = privateKeyString.substring(begin.length(),
          privateKeyString.lastIndexOf(end));
    }
    return getPrivateKey(Base64.decode(privateKeyString));
  }

  /**
   * Retrieves a {@link java.security.PrivateKey} from an array of bytes.
   *
   * @param privateKeyBytes The array of bytes from which to load the private
   *     key.
   * @return A {@link java.security.PrivateKey} object.
   * @throws InvalidKeySpecException
   * @throws NoSuchAlgorithmException
   */
  public static PrivateKey getPrivateKey(byte[] privateKeyBytes)
      throws InvalidKeySpecException, NoSuchAlgorithmException {
    KeyFactory fac = KeyFactory.getInstance("RSA");
    EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    return fac.generatePrivate(privKeySpec);
  }

  /** Converts the contents of a {@link java.io.Reader} object to a string. */
  private static String readToString(Reader in) throws IOException {
    StringBuffer buf = new StringBuffer();
    try {
      for (int c = in.read(); c != -1; c = in.read()) {
        buf.append((char) c);
      }
      return buf.toString();
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        in.close();
      } catch (Exception e) {
        // ignored
      }
    }
  }
}
