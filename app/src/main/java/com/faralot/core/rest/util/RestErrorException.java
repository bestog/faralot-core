package com.faralot.core.rest.util;

import java.io.IOException;

/**
 * Exception: Eigene Fehlerbehandlung meiner REST-API
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class RestErrorException extends IOException {

  public RestErrorException() { }

  public RestErrorException(String message) { super(message); }

  public RestErrorException(String message, Throwable cause) { super(message, cause); }

  public RestErrorException(Throwable cause) { super(cause); }
}
