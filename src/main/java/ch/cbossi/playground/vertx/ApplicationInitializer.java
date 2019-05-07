package ch.cbossi.playground.vertx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class ApplicationInitializer {

  private static final Logger LOGGER = Logger.getLogger(ApplicationInitializer.class.getName());

  private final Vertx vertx;
  private final List<Module> overrides;
  private Optional<Handler<AsyncResult<String>>> completionHandler;

  public static ApplicationInitializer of(Vertx vertx) {
    return new ApplicationInitializer(vertx);
  }

  private ApplicationInitializer(Vertx vertx) {
    this.vertx = vertx;
    this.overrides = new ArrayList<>();
    this.completionHandler = Optional.empty();
  }

  public ApplicationInitializer withCompletionHandler(Handler<AsyncResult<String>> completionHandler) {
    this.completionHandler = Optional.of(completionHandler);
    return this;
  }

  public ApplicationInitializer withOverrideIf(boolean condition, Supplier<Module> moduleSupplier) {
    if (condition) {
      return withOverride(moduleSupplier.get());
    }
    return this;
  }

  public ApplicationInitializer withOverride(Module module) {
    this.overrides.add(module);
    return this;
  }

  public void deploy() {
    ConfigRetriever.create(vertx).getConfig(config -> {
      logApplicationStart();
      Verticle verticle = createInjector(config.result()).getInstance(PlaygroundVerticle.class);
      Handler<AsyncResult<String>> completionHandler = this.completionHandler.orElseGet(logVerticleDeploymentFinished(verticle));
      vertx.deployVerticle(verticle, new DeploymentOptions().setConfig(config.result()), completionHandler);
    });
  }

  private void logApplicationStart() {
    if (hasOverrides()) {
      String overrideModules = overrides.stream()
          .map(Module::toString)
          .collect(joining(",", "[", "]"));
      LOGGER.info("Starting application with the following override-modules: " + overrideModules);
    } else {
      LOGGER.info("Starting application without override-modules.");
    }
  }

  private Supplier<Handler<AsyncResult<String>>> logVerticleDeploymentFinished(Verticle verticle) {
    return () -> result -> {
      LOGGER.info(format("Finished deploying verticle %s: %s", verticle.getClass().getSimpleName(), result.result()));
    };
  }

  private Injector createInjector(JsonObject config) {
    return Guice.createInjector(createModule(config));
  }

  private Module createModule(JsonObject config) {
    Module module = new PlaygroundModule(this.vertx, config);
    return hasOverrides() ? Modules.override(module).with(overrides) : module;
  }

  private boolean hasOverrides() {
    return !this.overrides.isEmpty();
  }
}
