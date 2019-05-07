package ch.cbossi.playground.vertx;


import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxTestContext;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class IntegrationTestCase {

  private static final String SCHEMA = "vertx";

  static WebClient webClient;
  private static Flyway flyway;

  @BeforeAll
  static void setup(Vertx vertx, VertxTestContext testContext) {
    ConfigRetriever.create(vertx).getConfig(config -> {
      migrateDb(config);
      startApplication(vertx, testContext);
      webClient = createWebClient(vertx);
    });
  }

  private static void migrateDb(AsyncResult<JsonObject> config) {
    flyway = new Flyway();
    DbConfig dbConfig = DbConfig.fromRootConfig(config.result());
    flyway.setDataSource(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
    flyway.setSchemas(SCHEMA);
    flyway.clean();
    flyway.migrate();
  }

  private static void startApplication(Vertx vertx, VertxTestContext testContext) {
    ApplicationInitializer.of(vertx)
        .withCompletionHandler(testContext.completing())
        .withOverride(new MockModule())
        .deploy();
  }

  private static WebClient createWebClient(Vertx vertx) {
    WebClientOptions options = new WebClientOptions().setDefaultPort(8081).setDefaultHost("localhost");
    return WebClient.create(vertx, options);
  }

  @AfterAll
  static void teardown(Vertx vertx) {
    flyway.clean();
    vertx.close();
  }
}
