package ch.cbossi.playground.vertx;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.jooq.DSLContext;

class PlaygroundModule extends AbstractModule {

  private final Vertx vertx;
  private final JsonObject config;

  PlaygroundModule(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
    bind(JsonObject.class).annotatedWith(Config.class).toInstance(config);
    bind(DSLContext.class).toProvider(DSLContextProvider.class).in(Singleton.class);
    bind(PlaygroundService.class).to(PlaygroundServiceImpl.class);
  }
}
