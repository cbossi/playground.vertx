package ch.cbossi.playground.vertx;

import io.vertx.core.json.JsonObject;

public final class DbConfig {

  private final String url;
  private final String username;
  private final String password;

  public static DbConfig fromRootConfig(JsonObject config) {
    JsonObject dbConfig = config.getJsonObject("db");
    return dbConfig.mapTo(DbConfig.class);
  }

  /**
   * @deprecated Solely required for deserialization.
   */
  @Deprecated
  protected DbConfig() {
    this(null, null, null);
  }

  private DbConfig(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
