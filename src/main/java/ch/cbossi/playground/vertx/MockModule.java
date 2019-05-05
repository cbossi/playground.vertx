package ch.cbossi.playground.vertx;

class MockModule extends NamedModule {

  @Override
  protected void configure() {
    bind(PlaygroundService.class).to(PlaygroundServiceMock.class);
  }

}
