package ch.cbossi.playground.vertx;

import ch.cbossi.playground.vertx.tables.pojos.Person;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ch.cbossi.playground.vertx.Tables.PERSON;

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

  public Person getGreeting(String username) {
    return db.select(PERSON.NAME)
        .from(PERSON)
        .where(PERSON.USERNAME.eq(username))
        .fetchOneInto(Person.class);
  }
}
