package ch.cbossi.playground.vertx;

import java.util.Map;

final class Uris {

  public static String pathParam(String paramName) {
    return ":" + paramName;
  }

  public static String createUri(String urlPattern, Map<String, Object> params) {
    String url = urlPattern;
    for (String paramName : params.keySet()) {
      url = url.replace(pathParam(paramName), params.get(paramName).toString());
    }
    return url;
  }

  private Uris() {

  }
}
