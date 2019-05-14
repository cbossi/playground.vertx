package ch.cbossi.playground.vertx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class ApplicationInitializer {

  private static final Logger LOGGER = Logger.getLogger(ApplicationInitializer.class.getName());

  private static BiConsumer<Vertx, JsonObject> NULL_ON_BEFORE_HANDLER = (vertx, config) -> {
  };

  private final Vertx vertx;
  private final List<Module> modules;
  private final List<String> configFiles;
  private BiConsumer<Vertx, JsonObject> onBeforeStart;
  private Optional<Handler<AsyncResult<String>>> completionHandler;

  public static ApplicationInitializer of(Vertx vertx) {
    return new ApplicationInitializer(vertx);
  }

  private ApplicationInitializer(Vertx vertx) {
    this.vertx = vertx;
    this.modules = new ArrayList<>();
    this.configFiles = new ArrayList<>();
    this.onBeforeStart = NULL_ON_BEFORE_HANDLER;
    this.completionHandler = Optional.empty();
  }

  public ApplicationInitializer withModuleIf(boolean condition, Supplier<Module> moduleSupplier) {
    if (condition) {
      return withModule(moduleSupplier.get());
    }
    return this;
  }

  public ApplicationInitializer withModule(Module module) {
    this.modules.add(module);
    return this;
  }

  public ApplicationInitializer withConfigFile(String configFilePath) {
    configFiles.add(configFilePath);
    return this;
  }

  public ApplicationInitializer withCompletionHandler(Handler<AsyncResult<String>> completionHandler) {
    this.completionHandler = Optional.of(completionHandler);
    return this;
  }

  public ApplicationInitializer onBeforeStart(BiConsumer<Vertx, JsonObject> onBeforeStart) {
    this.onBeforeStart = onBeforeStart;
    return this;
  }

  public void deploy() {
    ConfigRetriever.create(vertx, createConfigOptions()).getConfig(config -> {
      onBeforeStart.accept(vertx, config.result());
      logApplicationStart();
      Verticle verticle = createInjector(config.result()).getInstance(PlaygroundVerticle.class);
      Handler<AsyncResult<String>> completionHandler = this.completionHandler.orElseGet(logVerticleDeploymentFinished(verticle));
      vertx.deployVerticle(verticle, new DeploymentOptions().setConfig(config.result()), completionHandler);
    });
  }

  private ConfigRetrieverOptions createConfigOptions() {
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().setIncludeDefaultStores(true);
    configFiles.stream()
        .map(this::createFileConfigStore)
        .forEach(options::addStore);
    return options;
  }

  private ConfigStoreOptions createFileConfigStore(String configFilePath) {
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
        .setType("file")
        .setConfig(new JsonObject().put("path", configFilePath));
    return fileStore;
  }

  private void logApplicationStart() {
    String modulesAsString = modules.stream()
        .map(Module::toString)
        .collect(joining(",", "[", "]"));
    String configFilesAsString = configFiles.stream()
        .collect(joining(",", "[", "]"));
    LOGGER.info(format("Starting application with module overrides %s and additional config files %s.", modulesAsString, configFilesAsString));
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
    return !this.modules.isEmpty() ? Modules.override(module).with(modules) : module;
  }

}
