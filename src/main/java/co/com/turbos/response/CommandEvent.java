package co.com.turbos.response;

import com.google.gson.Gson;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CommandEvent<E> {

  private E request;
  private Map<String, String> params;

  public CommandEvent(E request, Map<String, String> params) {
    this.request = request;
    this.params = params;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}