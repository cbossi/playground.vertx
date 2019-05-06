package ch.cbossi.playground.vertx;

import io.vertx.core.Future;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class WorkerTest {

  /**
   * If it is guaranteed that a future is returned synchronously (e.g. since the real service is mocked using {@link Future#succeededFuture()},
   * we can use {@link Future#result()} to do assertion on the result (which would return {@code null} if the future did not yet complete).
   */
  @Test
  public void testSynchronousFuture() {
    Worker worker = new Worker();

    Future<String> asyncResult = worker.doSyncWork();

    assertThat(asyncResult.result()).isEqualTo("Synchronous Content");
  }

  /**
   * See: https://vertx.io/docs/vertx-junit5/java/
   */
  @Test
  @Timeout(value = 3, timeUnit = TimeUnit.SECONDS) // this optional annotation configures the timeout of the testContext
  public void testAsynchronousFuture(VertxTestContext testContext) throws InterruptedException {
    Worker worker = new Worker();

    // To make sure that VertxTestContext is notified of failures, you need to wrap assertions with a call to verify:
    // https://vertx.io/docs/vertx-junit5/java/#_use_any_assertion_library
    worker.doAsyncWork().setHandler(asyncResult -> testContext.verify(() -> {
      assertThat(asyncResult.result()).isEqualTo("Asynchronous Content");
      testContext.completeNow();
    }));
  }

}
