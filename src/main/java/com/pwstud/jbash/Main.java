package com.pwstud.jbash;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.pwstud.jbash.shell.input.EventBus;
import com.pwstud.jbash.shell.input.EventListener;
import com.pwstud.jbash.shell.input.JBashTerminal;

public class Main {

  public static void main(String[] args) {
    // LogsManager.initLogRedirection();
    // Debug.log("Started Engine!", "\n");
    // try {
    // while (true) {
    // String exitCode = ProcessManager.getProcessByID("system").stdout(null);
    // if (exitCode.equals("0"))
    // return;
    // }
    // } catch (Exception e) {
    // Debug.logError(e);
    // }

    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Custom Terminal");

      // Create the event bus
      EventBus eventBus = new EventBus();

      EventListener eventListener = new EventListener() {
        public void onMousePress(int col, int row, int button) {

        }
      };

      // Add a simple listener for demonstration
      eventBus.register(eventListener);

      // Launch terminal
      JBashTerminal term = new JBashTerminal(frame, eventBus);
      term.getAutoCompleter().addAll(List.of("help", "hello", "exit", "exec", "echo"));
    });
  }
}