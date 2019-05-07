package ch.cbossi.playground.vertx;

import ch.cbossi.playground.vertx.tables.pojos.Person;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Inject;
import java.util.logging.Logger;

import static ch.cbossi.playground.vertx.Uris.pathParam;
import static java.lang.String.format;

class PlaygroundController {

  static final String NAME_PARAM = "name";
  static final String GREETINGS_URL = "/greetings/";
  static final String GREETING_URL = GREETINGS_URL + pathParam(NAME_PARAM);

  private final PlaygroundService service;
  private final PlaygroundRepository repository;
  private final Logger logger;

  @Inject
  PlaygroundController(PlaygroundService service, PlaygroundRepository repository, Logger logger) {
    this.service = service;
    this.repository = repository;
    this.logger = logger;
  }

  public void create(RoutingContext routingContext) {
    Person person = routingContext.getBodyAsJson().mapTo(Person.class);
    Person persistedPerson = repository.insert(person);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(persistedPerson));
  }

  public void greeting(RoutingContext routingContext) {
    String name = routingContext.request().getParam(NAME_PARAM);
    logger.info(format("Fetch greeting for %s.", name));
    Person person = this.repository.getGreeting(name);
    GreetingTO greeting = new GreetingTO(service.getGreeting() + (person != null ? person.getName() : name));

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(greeting));
  }

  static class GreetingTO {
    private final String greeting;

    GreetingTO() {
      this(null);
    }

    private GreetingTO(String greeting) {
      this.greeting = greeting;
    }

    public String getGreeting() {
      return greeting;
    }
  }
}
