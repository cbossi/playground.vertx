package ch.cbossi.playground.vertx;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.json.JsonObject;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

class DSLContextProvider implements Provider<DSLContext> {

  private final Logger logger;
  private final JsonObject dbConfig;

  @Inject
  public DSLContextProvider(@Config JsonObject config, Logger logger) {
    this.logger = logger;
    this.dbConfig = config.getJsonObject("db");
  }

  @Override
  public DSLContext get() {
    logger.info("Opening database connection.");
    try {
      String url = dbConfig.getString("url");
      String username = dbConfig.getString("username");
      String password = dbConfig.getString("password");
      Connection connection = DriverManager.getConnection(url, username, password);
      return DSL.using(connection, SQLDialect.POSTGRES);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
