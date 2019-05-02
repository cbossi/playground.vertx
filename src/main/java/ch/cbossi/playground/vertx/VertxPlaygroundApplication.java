package ch.cbossi.playground.vertx;

import io.vertx.core.Vertx;

public class VertxPlaygroundApplication {

  public static void main(String[] args) {
    System.setProperty("vertx.disableDnsResolver", "true"); // see https://github.com/eclipse-vertx/vert.x/issues/2369#issuecomment-439722563
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PlaygroundVerticle(vertx));
  }

}
