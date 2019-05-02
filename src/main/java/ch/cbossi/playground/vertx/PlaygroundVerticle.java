package ch.cbossi.playground.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

class PlaygroundVerticle extends AbstractVerticle {

  private final Vertx vertx;

  private HttpServer httpServer;

  PlaygroundVerticle(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public void start(Future<Void> future) {
    httpServer = vertx.createHttpServer()
        .requestHandler(request -> request.response().end("Hello Vert.x"))
        .listen(8080, httpServer -> {
          if (httpServer.succeeded()) {
            future.complete();
          } else {
            future.fail(httpServer.cause());
          }
        });
  }

  @Override
  public void stop() {
    httpServer.close();
  }
}
