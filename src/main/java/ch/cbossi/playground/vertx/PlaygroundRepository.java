package ch.cbossi.playground.vertx;

import ch.cbossi.playground.vertx.tables.pojos.Person;
import org.jooq.DSLContext;

import javax.inject.Inject;

import static ch.cbossi.playground.vertx.Tables.PERSON;

class PlaygroundRepository {

  private final DSLContext db;

  @Inject
  PlaygroundRepository(DSLContext db) {
    this.db = db;
  }


  public Person getGreeting(String username) {
    return db.select(PERSON.NAME)
        .from(PERSON)
        .where(PERSON.USERNAME.eq(username))
        .fetchOneInto(Person.class);
  }
}
