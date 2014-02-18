// Code generated by dagger-compiler.  Do not edit.
package com.example.myweatherreporter.activity;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<AddCityActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code AddCityActivity} and its
 * dependencies.
 *
 * Being a {@code Provider<AddCityActivity>} and handling creation and
 * preparation of object instances.
 *
 * Being a {@code MembersInjector<AddCityActivity>} and handling injection
 * of annotated fields.
 */
public final class AddCityActivity$$InjectAdapter extends Binding<AddCityActivity>
    implements Provider<AddCityActivity>, MembersInjector<AddCityActivity> {
  private Binding<com.example.myweatherreporter.listadapter.CustomCityListAdapter> adapter;
  private Binding<com.example.myweatherreporter.beans.City> city;
  private Binding<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>> cityWeatherList;
  private Binding<Provider<com.example.myweatherreporter.beans.CityWeather>> cityWeatherProvider;
  private Binding<com.example.myweatherreporter.db.DatabaseHandler> dbHandler;
  private Binding<android.location.Geocoder> geocoder;
  private Binding<android.location.LocationManager> locationManager;
  private Binding<com.example.myweatherreporter.widget.CustomProgressDialog> progressDialog;
  private Binding<AddCityBaseActivity> supertype;

  public AddCityActivity$$InjectAdapter() {
    super("com.example.myweatherreporter.activity.AddCityActivity", "members/com.example.myweatherreporter.activity.AddCityActivity", NOT_SINGLETON, AddCityActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    adapter = (Binding<com.example.myweatherreporter.listadapter.CustomCityListAdapter>) linker.requestBinding("com.example.myweatherreporter.listadapter.CustomCityListAdapter", AddCityActivity.class, getClass().getClassLoader());
    city = (Binding<com.example.myweatherreporter.beans.City>) linker.requestBinding("com.example.myweatherreporter.beans.City", AddCityActivity.class, getClass().getClassLoader());
    cityWeatherList = (Binding<java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>>) linker.requestBinding("java.util.ArrayList<com.example.myweatherreporter.beans.CityWeather>", AddCityActivity.class, getClass().getClassLoader());
    cityWeatherProvider = (Binding<Provider<com.example.myweatherreporter.beans.CityWeather>>) linker.requestBinding("javax.inject.Provider<com.example.myweatherreporter.beans.CityWeather>", AddCityActivity.class, getClass().getClassLoader());
    dbHandler = (Binding<com.example.myweatherreporter.db.DatabaseHandler>) linker.requestBinding("com.example.myweatherreporter.db.DatabaseHandler", AddCityActivity.class, getClass().getClassLoader());
    geocoder = (Binding<android.location.Geocoder>) linker.requestBinding("android.location.Geocoder", AddCityActivity.class, getClass().getClassLoader());
    locationManager = (Binding<android.location.LocationManager>) linker.requestBinding("android.location.LocationManager", AddCityActivity.class, getClass().getClassLoader());
    progressDialog = (Binding<com.example.myweatherreporter.widget.CustomProgressDialog>) linker.requestBinding("com.example.myweatherreporter.widget.CustomProgressDialog", AddCityActivity.class, getClass().getClassLoader());
    supertype = (Binding<AddCityBaseActivity>) linker.requestBinding("members/com.example.myweatherreporter.activity.AddCityBaseActivity", AddCityActivity.class, getClass().getClassLoader(), false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(adapter);
    injectMembersBindings.add(city);
    injectMembersBindings.add(cityWeatherList);
    injectMembersBindings.add(cityWeatherProvider);
    injectMembersBindings.add(dbHandler);
    injectMembersBindings.add(geocoder);
    injectMembersBindings.add(locationManager);
    injectMembersBindings.add(progressDialog);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<AddCityActivity>}.
   */
  @Override
  public AddCityActivity get() {
    AddCityActivity result = new AddCityActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<AddCityActivity>}.
   */
  @Override
  public void injectMembers(AddCityActivity object) {
    object.adapter = adapter.get();
    object.city = city.get();
    object.cityWeatherList = cityWeatherList.get();
    object.cityWeatherProvider = cityWeatherProvider.get();
    object.dbHandler = dbHandler.get();
    object.geocoder = geocoder.get();
    object.locationManager = locationManager.get();
    object.progressDialog = progressDialog.get();
    supertype.injectMembers(object);
  }

}