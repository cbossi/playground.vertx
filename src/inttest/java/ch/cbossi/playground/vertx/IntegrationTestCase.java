package ch.cbossi.playground.vertx;


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
    ApplicationInitializer.of(vertx)
        .withCompletionHandler(testContext.completing())
        .withModule(new MockModule())
        .withConfigFile("inttest.json")
        .onBeforeStart(IntegrationTestCase::migrateDb)
        .deploy();
    webClient = createWebClient(vertx);
  }

  private static void migrateDb(JsonObject config) {
    flyway = new Flyway();
    DbConfig dbConfig = DbConfig.fromRootConfig(config);
    flyway.setDataSource(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
    flyway.setSchemas(SCHEMA);
    flyway.clean();
    flyway.migrate();
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
