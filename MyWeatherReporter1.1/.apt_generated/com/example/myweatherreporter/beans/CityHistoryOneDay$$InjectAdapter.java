// Code generated by dagger-compiler.  Do not edit.
package com.example.myweatherreporter.beans;

import dagger.internal.Binding;
import javax.inject.Provider;

/**
 * A {@code Binding<CityHistoryOneDay>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Being a {@code Provider<CityHistoryOneDay>} and handling creation and
 * preparation of object instances.
 */
public final class CityHistoryOneDay$$InjectAdapter extends Binding<CityHistoryOneDay>
    implements Provider<CityHistoryOneDay> {

  public CityHistoryOneDay$$InjectAdapter() {
    super("com.example.myweatherreporter.beans.CityHistoryOneDay", "members/com.example.myweatherreporter.beans.CityHistoryOneDay", NOT_SINGLETON, CityHistoryOneDay.class);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<CityHistoryOneDay>}.
   */
  @Override
  public CityHistoryOneDay get() {
    CityHistoryOneDay result = new CityHistoryOneDay();
    return result;
  }

}
