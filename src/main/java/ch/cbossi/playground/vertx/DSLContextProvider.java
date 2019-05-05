package ch.cbossi.playground.vertx;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

class DSLContextProvider implements Provider<DSLContext> {

  private static final String URL = "jdbc:postgresql://localhost:5432/playground";
  private static final String USERNAME = "playground";
  private static final String PASSWORD = "playgroundpw";

  private final Logger logger;

  @Inject
  public DSLContextProvider(Logger logger) {
    this.logger = logger;
  }

  @Override
  public DSLContext get() {
    logger.info("Opening database connection.");
    try {
      Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      return DSL.using(connection, SQLDialect.POSTGRES);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
