package ch.cbossi.playground.vertx;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class Modules {

  public static <T> Module singleBinding(Class<T> type, T instance) {
    return new AbstractModule() {
      @Override
      protected void configure() {
        bind(type).toInstance(instance);
      }
    };
  }

}
