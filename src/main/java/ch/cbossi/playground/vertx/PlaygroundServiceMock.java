package ch.cbossi.playground.vertx;

class PlaygroundServiceMock implements PlaygroundService {
  @Override
  public String getGreeting() {
    return "Hello mocked ";
  }
}
