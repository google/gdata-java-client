package mashups.eventpub;

/**
 * Authentication exception class for the EventPublisher 
 *
 * @author api.rboyd@google.com (Ryan Boyd)
 */ 
public class EPAuthenticationException extends Exception {

  public static final long serialVersionUID = 1L;
  
  public EPAuthenticationException(String message) {
    super(message);
  }

  public EPAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public EPAuthenticationException(Throwable cause) {
    super(cause);
  }
}
