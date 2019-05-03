package ch.cbossi.playground.vertx;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

class PlaygroundRepository {

  private static final String URL = "jdbc:postgresql://localhost:5432/playground";
  private static final String USERNAME = "playground";
  private static final String PASSWORD = "playgroundpw";

  private final DSLContext db;

  PlaygroundRepository() {
    this.db = createDb();
  }

  private DSLContext createDb() {
    try {
      Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      return DSL.using(connection, SQLDialect.POSTGRES);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String getGreeting(String username) {
    return db.select(field("name"))
        .from(table("vertx.person"))
        .where(field("username").eq(username))
        .fetchOneInto(String.class);
  }
}
