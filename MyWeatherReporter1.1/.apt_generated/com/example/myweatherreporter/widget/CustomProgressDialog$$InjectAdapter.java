// Code generated by dagger-compiler.  Do not edit.
package com.example.myweatherreporter.widget;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<CustomProgressDialog>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code CustomProgressDialog} and its
 * dependencies.
 *
 * Being a {@code Provider<CustomProgressDialog>} and handling creation and
 * preparation of object instances.
 */
public final class CustomProgressDialog$$InjectAdapter extends Binding<CustomProgressDialog>
    implements Provider<CustomProgressDialog> {
  private Binding<android.content.Context> context;

  public CustomProgressDialog$$InjectAdapter() {
    super("com.example.myweatherreporter.widget.CustomProgressDialog", "members/com.example.myweatherreporter.widget.CustomProgressDialog", NOT_SINGLETON, CustomProgressDialog.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    context = (Binding<android.content.Context>) linker.requestBinding("android.content.Context", CustomProgressDialog.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    getBindings.add(context);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<CustomProgressDialog>}.
   */
  @Override
  public CustomProgressDialog get() {
    CustomProgressDialog result = new CustomProgressDialog(context.get());
    return result;
  }

}
