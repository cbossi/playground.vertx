package ch.cbossi.playground.vertx;


import io.vertx.core.Future;

public class Worker {

  public Future<String> doSyncWork() {
    return Future.succeededFuture("Synchronous Content");
  }

  public Future<String> doAsyncWork() {
    return Future.future(handler -> {
      new Thread(() -> {
        try {
          Thread.sleep(500);
          handler.complete("Asynchronous Content");
        } catch (InterruptedException e) {
          handler.failed();
        }
      }).start();
    });
  }

}
