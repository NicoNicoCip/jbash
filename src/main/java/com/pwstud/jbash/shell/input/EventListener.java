package com.pwstud.jbash.shell.input;

import java.awt.event.KeyEvent;

public interface EventListener {
  default void onResize(int columns, int rows) {
  }

  default void onMouseMove(int col, int row) {
  }

  default void onChar(char c) {
  }

  default void onKey(KeyEvent e) {
  }

  default void onCommand(String command) {
  }

  default void onMouseClick(int col, int row, int button) {

  }

  default void onMousePress(int col, int row, int button) {

  }

  default void onMouseRelease(int col, int row, int button) {

  }

  default void onAutocomplete(String completer) {
    
  }
}
