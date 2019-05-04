package ch.cbossi.playground.vertx;

import com.google.inject.AbstractModule;

class MockModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PlaygroundService.class).to(PlaygroundServiceMock.class);
  }

}
