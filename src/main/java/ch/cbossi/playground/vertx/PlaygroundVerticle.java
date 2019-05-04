package ch.cbossi.playground.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import javax.inject.Inject;

import static ch.cbossi.playground.vertx.PlaygroundController.GREETING_URL;

class PlaygroundVerticle extends AbstractVerticle {

  private final Vertx vertx;
  private final PlaygroundController controller;

  private HttpServer httpServer;

  @Inject
  public PlaygroundVerticle(Vertx vertx, PlaygroundController controller) {
    this.vertx = vertx;
    this.controller = controller;
  }

  @Override
  public void start(Future<Void> future) {
    httpServer = vertx.createHttpServer()
        .requestHandler(router())
        .listen(config().getInteger("http.port"), httpServer -> {
          if (httpServer.succeeded()) {
            future.complete();
          } else {
            future.fail(httpServer.cause());
          }
        });
  }

  private Router router() {
    Router router = Router.router(vertx);
    router.get(GREETING_URL).handler(controller::greeting);
    return router;
  }

  @Override
  public void stop() {
    httpServer.close();
  }
}
