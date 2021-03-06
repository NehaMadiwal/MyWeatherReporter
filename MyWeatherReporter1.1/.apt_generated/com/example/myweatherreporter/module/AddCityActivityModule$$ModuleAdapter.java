// Code generated by dagger-compiler.  Do not edit.
package com.example.myweatherreporter.module;

import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import java.util.Map;
import java.util.Set;
import javax.inject.Provider;

/**
 * A manager of modules and provides adapters allowing for proper linking and
 * instance provision of types served by {@code @Provides} methods.
 */
public final class AddCityActivityModule$$ModuleAdapter extends ModuleAdapter<AddCityActivityModule> {
  private static final String[] INJECTS = { "members/com.example.myweatherreporter.activity.AddCityActivity", };
  private static final Class<?>[] STATIC_INJECTIONS = { };
  private static final Class<?>[] INCLUDES = { };

  public AddCityActivityModule$$ModuleAdapter() {
    super(com.example.myweatherreporter.module.AddCityActivityModule.class, INJECTS, STATIC_INJECTIONS, false /*overrides*/, INCLUDES, true /*complete*/, true /*library*/);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getBindings(Map<String, Binding<?>> map, AddCityActivityModule module) {
    map.put("android.location.LocationManager", new ProvideLocationManagerProvidesAdapter(module));
    map.put("java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>", new ProvideCityWeatherArrayListProvidesAdapter(module));
    map.put("android.content.Context", new ProvideActivityContextProvidesAdapter(module));
    map.put("android.location.Geocoder", new ProvideGeocoderProvidesAdapter(module));
    map.put("com.example.myweatherreporter.listadapter.CustomCityListAdapter", new ProvideCustomCityListAdapterProvidesAdapter(module));
  }

  /**
   * A {@code Binding<android.location.LocationManager>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<android.location.LocationManager>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideLocationManagerProvidesAdapter extends Binding<android.location.LocationManager>
      implements Provider<android.location.LocationManager> {
    private final AddCityActivityModule module;

    public ProvideLocationManagerProvidesAdapter(AddCityActivityModule module) {
      super("android.location.LocationManager", null, IS_SINGLETON, "com.example.myweatherreporter.module.AddCityActivityModule.provideLocationManager()");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<android.location.LocationManager>}.
     */
    @Override
    public android.location.LocationManager get() {
      return module.provideLocationManager();
    }
  }

  /**
   * A {@code Binding<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideCityWeatherArrayListProvidesAdapter extends Binding<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>>
      implements Provider<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>> {
    private final AddCityActivityModule module;

    public ProvideCityWeatherArrayListProvidesAdapter(AddCityActivityModule module) {
      super("java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>", null, IS_SINGLETON, "com.example.myweatherreporter.module.AddCityActivityModule.provideCityWeatherArrayList()");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>>}.
     */
    @Override
    public java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather> get() {
      return module.provideCityWeatherArrayList();
    }
  }

  /**
   * A {@code Binding<android.content.Context>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<android.content.Context>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideActivityContextProvidesAdapter extends Binding<android.content.Context>
      implements Provider<android.content.Context> {
    private final AddCityActivityModule module;

    public ProvideActivityContextProvidesAdapter(AddCityActivityModule module) {
      super("android.content.Context", null, IS_SINGLETON, "com.example.myweatherreporter.module.AddCityActivityModule.provideActivityContext()");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<android.content.Context>}.
     */
    @Override
    public android.content.Context get() {
      return module.provideActivityContext();
    }
  }

  /**
   * A {@code Binding<android.location.Geocoder>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<android.location.Geocoder>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideGeocoderProvidesAdapter extends Binding<android.location.Geocoder>
      implements Provider<android.location.Geocoder> {
    private final AddCityActivityModule module;

    public ProvideGeocoderProvidesAdapter(AddCityActivityModule module) {
      super("android.location.Geocoder", null, IS_SINGLETON, "com.example.myweatherreporter.module.AddCityActivityModule.provideGeocoder()");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<android.location.Geocoder>}.
     */
    @Override
    public android.location.Geocoder get() {
      return module.provideGeocoder();
    }
  }

  /**
   * A {@code Binding<com.example.myweatherreporter.listadapter.CustomCityListAdapter>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Owning the dependency links between {@code com.example.myweatherreporter.listadapter.CustomCityListAdapter} and its
   * dependencies.
   *
   * Being a {@code Provider<com.example.myweatherreporter.listadapter.CustomCityListAdapter>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideCustomCityListAdapterProvidesAdapter extends Binding<com.example.myweatherreporter.listadapter.CustomCityListAdapter>
      implements Provider<com.example.myweatherreporter.listadapter.CustomCityListAdapter> {
    private final AddCityActivityModule module;
    private Binding<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>> cityWeatherList;

    public ProvideCustomCityListAdapterProvidesAdapter(AddCityActivityModule module) {
      super("com.example.myweatherreporter.listadapter.CustomCityListAdapter", null, NOT_SINGLETON, "com.example.myweatherreporter.module.AddCityActivityModule.provideCustomCityListAdapter()");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Used internally to link bindings/providers together at run time
     * according to their dependency graph.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void attach(Linker linker) {
      cityWeatherList = (Binding<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>>) linker.requestBinding("java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>", AddCityActivityModule.class, getClass().getClassLoader());
    }

    /**
     * Used internally obtain dependency information, such as for cyclical
     * graph detection.
     */
    @Override
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
      getBindings.add(cityWeatherList);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<com.example.myweatherreporter.listadapter.CustomCityListAdapter>}.
     */
    @Override
    public com.example.myweatherreporter.listadapter.CustomCityListAdapter get() {
      return module.provideCustomCityListAdapter(cityWeatherList.get());
    }
  }
}
