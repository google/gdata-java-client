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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

/**
 * Signs strings using RSA-SHA1.
 *
 * 
 */
public class OAuthRsaSha1Signer implements OAuthSigner {

  PrivateKey privateKey;

  /**
   * Constructor for {@link OAuthRsaSha1Signer}.  This form of the constructor
   * does not set the private key, so be sure to call
   * {@link #setPrivateKey(PrivateKey)} before signing requests.
   */
  public OAuthRsaSha1Signer() {
  }

  /**
   * Sets the RSA-SHA1 private key object used to sign this request.
   *
   * @param privateKey the {@link java.security.PrivateKey} to use to initialize
   *        the signer.
   * @throws OAuthException if setting the private key fails
   */
  public OAuthRsaSha1Signer(PrivateKey privateKey) throws OAuthException {
    setPrivateKey(privateKey);
  }

  /**
   * Sets the RSA-SHA1 private key object used to sign this request.
   *
   * @param privateKeyString the Base-64 encoded private key string conforming
            to the PKCS #8 standard.
   * @throws OAuthException if setting the private key fails
   */
  public OAuthRsaSha1Signer(String privateKeyString) throws OAuthException {
    if (privateKeyString == null) {
      throw new OAuthException("Private key string cannot be null");
    } else if (privateKeyString.length() == 0) {
      throw new OAuthException("Private key string cannot be empty");
    }
    try {
      setPrivateKey(
          RsaSha1PrivateKeyHelper.getPrivateKey(privateKeyString));
    } catch (Base64DecoderException e) {
      throw new OAuthException("Invalid private key", e);
    } catch (NoSuchAlgorithmException e) {
      throw new OAuthException("Invalid private key", e);
    } catch (InvalidKeySpecException e) {
      throw new OAuthException("Invalid private key", e);
    }
  }

  /**
   * Sets the RSA-SHA1 private key object used to sign this request.
   *
   * @param privateKeyBytes the Base-64 encoded private key conforming to the
   *        PKCS #8 standard.
   * @throws OAuthException if setting the private key fails
   */
  public OAuthRsaSha1Signer(byte[] privateKeyBytes) throws OAuthException {
    if (privateKeyBytes == null) {
      throw new OAuthException("Private key bytes cannot be null");
    } else if (privateKeyBytes.length == 0) {
      throw new OAuthException("Private key bytes cannot be empty");
    }
    try {
      setPrivateKey(
          RsaSha1PrivateKeyHelper.getPrivateKey(privateKeyBytes));
    } catch (NoSuchAlgorithmException e) {
      throw new OAuthException("Invalid private key", e);
    } catch (InvalidKeySpecException e) {
      throw new OAuthException("Invalid private key", e);
    }
  }

  /**
   * Sets the {@link java.security.PrivateKey} used to sign requests.
   *
   * @param privateKey
   * @throws OAuthException if the private key is null.
   */
  public void setPrivateKey(PrivateKey privateKey) throws OAuthException {
    if (privateKey == null) {
      throw new OAuthException("Private key cannot be null");
    }
    this.privateKey = privateKey;
  }

  public String getSignature(String baseString, OAuthParameters oauthParameters)
      throws OAuthException {
    if (privateKey == null) {
      throw new OAuthException("Private key cannot be null");
    }
    try {
      Signature signer = Signature.getInstance("SHA1withRSA");
      signer.initSign(privateKey);
      signer.update(baseString.getBytes("UTF-8"));
      return Base64.encode(signer.sign());
    } catch (NoSuchAlgorithmException e) {
      throw new OAuthException("Error generating signature", e);
    } catch (InvalidKeyException e) {
      throw new OAuthException("Error generating signature", e);
    } catch (SignatureException e) {
      throw new OAuthException("Error generating signature", e);
    } catch (UnsupportedEncodingException e) {
      throw new OAuthException("Error generating signature", e);
    }
  }

  public String getSignatureMethod() {
    return "RSA-SHA1";
  }

}
