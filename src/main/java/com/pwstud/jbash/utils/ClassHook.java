package com.pwstud.jbash.utils;

import org.reflections.Reflections;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class ClassHook {

  public static <T> List<T> getInstancesOfType(Class<T> type, String basePackage) {

    List<T> instances = new ArrayList<>();
    Reflections reflections = new Reflections(basePackage);

    Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(type);

    for (Class<? extends T> clazz : subTypes) {
      try {
        if (!clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
          T instance = clazz.getDeclaredConstructor().newInstance();
          instances.add(instance);
        }
      } catch (Exception e) {
        System.err.println("Failed to instantiate: " + clazz.getName());
      }
    }

    return instances;
  }
}
