package ch.cbossi.playground.vertx;

import ch.cbossi.playground.vertx.tables.pojos.Person;

import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

class PlaygroundRepositoryMock extends PlaygroundRepository {

  private final Map<String, Person> personsByUsername;

  PlaygroundRepositoryMock(Set<Person> persons) {
    super(null);
    this.personsByUsername = persons.stream().collect(toMap(Person::getUsername, identity()));
  }

  @Override
  public Person getGreeting(String username) {
    return this.personsByUsername.get(username);
  }
}
