package ch.cbossi.playground.vertx;

import com.google.inject.AbstractModule;

public abstract class NamedModule extends AbstractModule {

  @Override
  public final String toString() {
    return getName();
  }

  protected String getName() {
    return this.getClass().getSimpleName();
  }
}
