package ch.cbossi.playground.vertx;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static ch.cbossi.playground.vertx.Uris.pathParam;

class PlaygroundController {

  static final String NAME_PARAM = "name";
  static final String GREETING_URL = "/greetings/" + pathParam(NAME_PARAM);

  public void greeting(RoutingContext routingContext) {
    String name = routingContext.request().getParam(NAME_PARAM);
    GreetingTO greeting = new GreetingTO("Hello " + name);

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
