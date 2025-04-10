package com.ayungi.travelappserver.exception;

public class TokenNotValidException extends RuntimeException {
  public TokenNotValidException(String message) {
    super(message);
  }
}
