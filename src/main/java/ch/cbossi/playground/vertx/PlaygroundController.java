package ch.cbossi.playground.vertx;

import ch.cbossi.playground.vertx.tables.pojos.Person;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Inject;

import static ch.cbossi.playground.vertx.Uris.pathParam;

class PlaygroundController {

  static final String NAME_PARAM = "name";
  static final String GREETING_URL = "/greetings/" + pathParam(NAME_PARAM);

  private final PlaygroundService service;
  private final PlaygroundRepository repository;

  @Inject
  PlaygroundController(PlaygroundService service, PlaygroundRepository repository) {
    this.service = service;
    this.repository = repository;
  }

  public void greeting(RoutingContext routingContext) {
    String name = routingContext.request().getParam(NAME_PARAM);
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
