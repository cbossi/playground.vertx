package ch.cbossi.playground.vertx;

import io.vertx.core.Vertx;

import static java.util.Arrays.asList;

public class VertxPlaygroundApplication {

  private static final String USE_MOCKS = "useMocks";

  public static void main(String[] args) {
    System.setProperty("vertx.disableDnsResolver", "true"); // see https://github.com/eclipse-vertx/vert.x/issues/2369#issuecomment-439722563
    ApplicationInitializer.of(Vertx.vertx())
        .withModuleIf(asList(args).contains(USE_MOCKS), MockModule::new)
        .deploy();
  }

}
