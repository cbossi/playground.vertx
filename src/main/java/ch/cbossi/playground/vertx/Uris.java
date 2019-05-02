package ch.cbossi.playground.vertx;

final class Uris {

  public static String pathParam(String paramName) {
    return ":" + paramName;
  }

  private Uris() {

  }
}
