package ch.cbossi.playground.vertx;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import org.jooq.DSLContext;

class PlaygroundModule extends AbstractModule {

  private final Vertx vertx;

  PlaygroundModule(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
    bind(DSLContext.class).toProvider(DSLContextProvider.class).in(Singleton.class);
    bind(PlaygroundService.class).to(PlaygroundServiceImpl.class);
  }
}
