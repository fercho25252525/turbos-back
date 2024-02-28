package co.com.turbos.response;


import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseEvent<T> {

  /**
   * Codigo de respuesta
   */
  private ResponseCode code;
  /**
   * Mensaje de respuesta
   */
  private String message;

  /**
   * data
   */
  private T data;

  /**
   * @param message
   * @param data
   * @return
   */
  public ResponseEvent<T> ok(String message, T data) {
    setCode(ResponseCode.OK);
    setMessage(message);
    setData(data);
    return this;
  }

  public ResponseEvent<T> created(String message) {
    setCode(ResponseCode.CREATED);
    setMessage(message);
    return this;
  }

  public ResponseEvent<T> created(String message, T data) {
    setCode(ResponseCode.CREATED);
    setMessage(message);
    setData(data);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> noContent(String message, T data) {
    setCode(ResponseCode.NO_CONTENT);
    setMessage(message);
    setData(data);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> notFound(String message) {
    setCode(ResponseCode.NOT_FOUND);
    setMessage(message);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> notImplemented(String message) {
    setCode(ResponseCode.NOT_IMPLEMENTED);
    setMessage(message);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> badRequest(String message) {
    setCode(ResponseCode.BAD_REQUEST);
    setMessage(message);
    return this;
  }
  
  /**
   * @param message
   * @param data
   * @return
   */
  public ResponseEvent<T> badRequest(String message, T data) {
    setCode(ResponseCode.BAD_REQUEST);
    setMessage(message);
    setData(data);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> unauthorized(String message) {
    setCode(ResponseCode.UNAUTHORIZED);
    setMessage(message);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> forbidden(String message) {
    setCode(ResponseCode.FORBIDDEN);
    setMessage(message);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> conflict(String message) {
    setCode(ResponseCode.CONFLICT);
    setMessage(message);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> applicationError(String message) {
    setCode(ResponseCode.APPLICATION_ERROR);
    setMessage(message);
    return this;
  }

  public ResponseEvent<T> clientSecretError(String message, T data) {
    setCode(ResponseCode.INVALID_CLIENT_SECRET);
    setMessage(message);
    setData(data);
    return this;
  }

  /**
   * @param message
   * @return
   */
  public ResponseEvent<T> notAcceptable(String message) {
    setCode(ResponseCode.NOT_ACCEPTABLE);
    setMessage(message);
    return this;
  }

  public ResponseEvent<T> fail(String message) {
    setCode(ResponseCode.FAIL);
    setMessage(message);
    return this;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}