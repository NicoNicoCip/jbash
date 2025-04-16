package com.pwstud.jbash.processes;

import java.io.IOException;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.process.JBashProcess;

public class clear extends JBashProcess{
  @Override
  public String stdout(String[] stdin) {
    try {
      String os = System.getProperty("os.name").toLowerCase();

      if (os.contains("win")) {
        // For Windows
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        // For Unix-like systems
        new ProcessBuilder("clear").inheritIO().start().waitFor();
      }
      return "1";
    } catch (IOException | InterruptedException e) {
      Debug.logError(e);
      return "0";
    }
  }
}
