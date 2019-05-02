package ch.cbossi.playground.vertx;

import io.vertx.core.Vertx;

public class VertxPlaygroundApplication {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PlaygroundVerticle());
  }

}
