package ch.cbossi.playground.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import static ch.cbossi.playground.vertx.PlaygroundController.NAME_PARAM;
import static ch.cbossi.playground.vertx.Uris.pathParam;

class PlaygroundVerticle extends AbstractVerticle {

  private static final String GREETING_URL = "/greetings/" + pathParam(NAME_PARAM);

  private final Vertx vertx;
  private final PlaygroundController controller;

  private HttpServer httpServer;

  PlaygroundVerticle(Vertx vertx) {
    this.vertx = vertx;
    this.controller = new PlaygroundController();
  }

  @Override
  public void start(Future<Void> future) {
    httpServer = vertx.createHttpServer()
        .requestHandler(router())
        .listen(8080, httpServer -> {
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
