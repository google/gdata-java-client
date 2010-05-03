package com.google.api.client.auth.wrap;

import com.google.api.client.http.UriEntity;
import com.google.api.client.util.Name;

/**
 * Implements the Username and Password Profile of OAuth WRAP authorization as
 * specified in <a
 * href="http://tools.ietf.org/html/draft-hardt-oauth-01#section-6.1">OAuth Web
 * Resource Authorization Profiles</a>.
 */
public class UsernameAndPasswordProfile {

  public static class AccessTokenRequestUriEntity extends UriEntity {

    /** The Client Identifier (required). */
    @Name("wrap_client_id")
    public String clientId;

    /** The User's username (required). */
    @Name("wrap_username")
    public String username;

    /** The User's password (required). */
    @Name("wrap_password")
    public String password;

    /**
     * The Authorization Server MAY define authorization scope values for the
     * Client to include (optional).
     */
    @Name("wrap_scope")
    public String scope;

    public AccessTokenRequestUriEntity(String uri) {
      super(uri);
    }
  }

  private UsernameAndPasswordProfile() {
  }
}
