package ch.cbossi.playground.vertx;


import ch.cbossi.playground.vertx.PlaygroundController.GreetingTO;
import ch.cbossi.playground.vertx.tables.pojos.Person;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static ch.cbossi.playground.vertx.PlaygroundController.NAME_PARAM;
import static ch.cbossi.playground.vertx.Uris.createUri;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class IntegrationTest extends IntegrationTestCase {

  private static final String SAMPLE_USERNAME = "inttest";
  private static final String SAMPLE_NAME = "Integration Test";

  @BeforeEach
  void insertData(VertxTestContext testContext) {
    Person person = new Person().setUsername(SAMPLE_USERNAME).setName(SAMPLE_NAME);
    webClient.post(PlaygroundController.GREETINGS_URL)
        .sendJson(person, testContext.completing());
  }

  @Test
  void testGreeting(VertxTestContext testContext) {
    webClient.get(createUri(PlaygroundController.GREETING_URL, Map.of(NAME_PARAM, SAMPLE_USERNAME)))
        .send(testContext.succeeding(response -> testContext.verify(() -> {
          GreetingTO greeting = response.bodyAsJson(GreetingTO.class);
          assertThat(greeting.getGreeting()).isEqualTo("Hello mocked " + SAMPLE_NAME);
          testContext.completeNow();
        })));
  }
}
