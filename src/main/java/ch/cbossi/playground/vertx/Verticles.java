package ch.cbossi.playground.vertx;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

class Verticles {


  public static final Handler<AsyncResult<String>> NO_OPS_COMPLETION_HANDLER = result -> {
  };

  public static void deployVerticle(Vertx vertx) {
    deployVerticle(vertx, NO_OPS_COMPLETION_HANDLER);
  }

  public static void deployVerticle(Vertx vertx, Handler<AsyncResult<String>> completionHandler) {
    ConfigRetriever.create(vertx).getConfig(config -> {
      vertx.deployVerticle(new PlaygroundVerticle(vertx), new DeploymentOptions().setConfig(config.result()), completionHandler);
    });
  }

}
