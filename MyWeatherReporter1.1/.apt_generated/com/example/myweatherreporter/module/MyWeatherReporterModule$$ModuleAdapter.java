// Code generated by dagger-compiler.  Do not edit.
package com.example.myweatherreporter.module;

import dagger.internal.Binding;
import dagger.internal.ModuleAdapter;
import java.util.Map;
import javax.inject.Provider;

/**
 * A manager of modules and provides adapters allowing for proper linking and
 * instance provision of types served by {@code @Provides} methods.
 */
public final class MyWeatherReporterModule$$ModuleAdapter extends ModuleAdapter<MyWeatherReporterModule> {
  private static final String[] INJECTS = { };
  private static final Class<?>[] STATIC_INJECTIONS = { };
  private static final Class<?>[] INCLUDES = { };

  public MyWeatherReporterModule$$ModuleAdapter() {
    super(com.example.myweatherreporter.module.MyWeatherReporterModule.class, INJECTS, STATIC_INJECTIONS, false /*overrides*/, INCLUDES, true /*complete*/, true /*library*/);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getBindings(Map<String, Binding<?>> map, MyWeatherReporterModule module) {
    map.put("android.content.Context", new ProvideApplicationContextProvidesAdapter(module));
  }

  /**
   * A {@code Binding<android.content.Context>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<android.content.Context>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideApplicationContextProvidesAdapter extends Binding<android.content.Context>
      implements Provider<android.content.Context> {
    private final MyWeatherReporterModule module;

    public ProvideApplicationContextProvidesAdapter(MyWeatherReporterModule module) {
      super("android.content.Context", null, IS_SINGLETON, "com.example.myweatherreporter.module.MyWeatherReporterModule.provideApplicationContext()");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<android.content.Context>}.
     */
    @Override
    public android.content.Context get() {
      return module.provideApplicationContext();
    }
  }
}