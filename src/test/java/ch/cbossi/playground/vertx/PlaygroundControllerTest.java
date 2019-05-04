package ch.cbossi.playground.vertx;

import ch.cbossi.playground.vertx.PlaygroundController.GreetingTO;
import ch.cbossi.playground.vertx.tables.pojos.Person;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Set;

import static ch.cbossi.playground.vertx.Modules.singleBinding;
import static ch.cbossi.playground.vertx.PlaygroundController.NAME_PARAM;
import static ch.cbossi.playground.vertx.Uris.createUri;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class PlaygroundControllerTest {

  private static final Set<Person> PERSONS = Set.of(new Person().setUsername("test").setName("Test User"));

  private static WebClient webClient;

  @BeforeEach
  void deployVerticle(Vertx vertx, VertxTestContext testContext) {
    ApplicationInitializer.of(vertx)
        .withCompletionHandler(testContext.completing())
        .withOverride(new MockModule())
        .withOverride(singleBinding(PlaygroundRepository.class, new PlaygroundRepositoryMock(PERSONS)))
        .deploy();
    webClient = createWebClient(vertx);
  }

  private static WebClient createWebClient(Vertx vertx) {
    WebClientOptions options = new WebClientOptions().setDefaultPort(8080).setDefaultHost("localhost");
    return WebClient.create(vertx, options);
  }

  @Test
  void testGreeting(VertxTestContext testContext) {
    webClient.get(createUri(PlaygroundController.GREETING_URL, Map.of(NAME_PARAM, "test")))
        .send(testContext.succeeding(response -> testContext.verify(() -> {
          GreetingTO greeting = response.bodyAsJson(GreetingTO.class);
          assertThat(greeting.getGreeting()).isEqualTo("Hello mocked Test User");
          testContext.completeNow();
        })));
  }
}
