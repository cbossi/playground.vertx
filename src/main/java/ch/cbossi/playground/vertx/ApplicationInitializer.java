package ch.cbossi.playground.vertx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class ApplicationInitializer {

  private static final Handler<AsyncResult<String>> NO_OPS_COMPLETION_HANDLER = result -> {
  };

  private final Vertx vertx;
  private final List<Module> overrides;
  private Handler<AsyncResult<String>> completionHandler;

  public static ApplicationInitializer of(Vertx vertx) {
    return new ApplicationInitializer(vertx);
  }

  private ApplicationInitializer(Vertx vertx) {
    this.vertx = vertx;
    this.overrides = new ArrayList<>();
    this.completionHandler = NO_OPS_COMPLETION_HANDLER;
  }

  public ApplicationInitializer withCompletionHandler(Handler<AsyncResult<String>> completionHandler) {
    this.completionHandler = completionHandler;
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
      Verticle verticle = createInjector().getInstance(PlaygroundVerticle.class);
      vertx.deployVerticle(verticle, new DeploymentOptions().setConfig(config.result()), completionHandler);
    });
  }

  private Injector createInjector() {
    return Guice.createInjector(createModule());
  }

  private Module createModule() {
    Module module = new PlaygroundModule(this.vertx);
    return !overrides.isEmpty() ? Modules.override(module).with(overrides) : module;
  }
}
