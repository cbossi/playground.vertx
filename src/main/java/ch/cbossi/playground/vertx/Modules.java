package ch.cbossi.playground.vertx;

import com.google.inject.Module;

import static java.lang.String.format;

public class Modules {

  public static <T> Module singleBinding(Class<T> type, T instance) {
    return new NamedModule() {
      @Override
      protected void configure() {
        bind(type).toInstance(instance);
      }

      @Override
      protected String getName() {
        return format("single-binding: %s -> %s", type.getSimpleName(), instance.getClass().getSimpleName());
      }
    };
  }

}
