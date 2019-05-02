package ch.cbossi.playground.vertx;

import io.vertx.core.AbstractVerticle;

class PlaygroundVerticle extends AbstractVerticle {

  @Override
  public void start() {
    System.out.println("Hello Vert.x");
  }
}
