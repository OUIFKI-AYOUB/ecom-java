package com.example.Ecom.shared;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomResponseException extends RuntimeException {
  private int code;
  private String message;

  public static CustomResponseException ResourceNotFound(String message) {
    return new CustomResponseException(404, message);
  }


  public static CustomResponseException BadCredentials() {
    return new CustomResponseException(401, "Bad Credentials");
  }

  public static CustomResponseException BadRequest(String message) {
    return new CustomResponseException(400, message);
  }


  public static CustomResponseException Unauthorized(String s) {
    return new CustomResponseException(401, s);
  }
}
