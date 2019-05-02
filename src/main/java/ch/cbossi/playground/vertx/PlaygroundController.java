package ch.cbossi.playground.vertx;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

class PlaygroundController {

  static final String NAME_PARAM = "name";

  public void greeting(RoutingContext routingContext) {
    String name = routingContext.request().getParam(NAME_PARAM);
    GreetingTO greeting = new GreetingTO("Hello " + name);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(greeting));
  }

  private static class GreetingTO {
    private final String greeting;

    private GreetingTO(String greeting) {
      this.greeting = greeting;
    }

    public String getGreeting() {
      return greeting;
    }
  }
}
