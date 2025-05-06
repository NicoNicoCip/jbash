package com.pwstud.jbash.shell.input;

import java.util.*;

public class AutoCompleter {
  private final Set<String> completions = new HashSet<>();

  public void add(String word) {
    completions.add(word);
  }

  public void addAll(Collection<String> words) {
    completions.addAll(words);
  }

  public void remove(String word) {
    completions.remove(word);
  }

  public Optional<String> complete(String prefix) {
    return completions.stream()
        .filter(w -> w.startsWith(prefix))
        .sorted()
        .findFirst();
  }

  public List<String> completeAll(String prefix) {
    return completions.stream()
        .filter(opt -> opt.startsWith(prefix))
        .sorted()
        .toList();
  }

}
