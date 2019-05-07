package ch.cbossi.playground.vertx;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.json.JsonObject;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import static java.sql.DriverManager.getConnection;

class DSLContextProvider implements Provider<DSLContext> {

  private final Logger logger;
  private final DbConfig dbConfig;

  @Inject
  public DSLContextProvider(@Config JsonObject config, Logger logger) {
    this.logger = logger;
    this.dbConfig = DbConfig.fromRootConfig(config);
  }

  @Override
  public DSLContext get() {
    logger.info("Opening database connection.");
    try {
      Connection connection = getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
      return DSL.using(connection, SQLDialect.POSTGRES);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
