package ch.cbossi.playground.vertx;

class PlaygroundServiceImpl implements PlaygroundService {
  @Override
  public String getGreeting() {
    return "Hello real ";
  }
}
