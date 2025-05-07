package com.pwstud.jbash;

import java.util.Arrays;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.EventListener;
import com.pwstud.jbash.shell.process.ProcessManager;
import com.pwstud.jbash.utils.StringUtils;

public class Main {

  public static void main(String[] args) {
    Debug.log("Started Engine!", "\n");
    Debug.getTerminal().printPrompt();
    Debug.getTerminal().getAutoCompleter().addAll(Arrays.asList(
      "echo",
      "exit",
      "clear",
      "test"
    ));
    Debug.getTerminal().eventBus.register(new EventListener() {
      @Override
      public void onCommand(String command) {
        if(command.startsWith("echo")) {
          String[] com = StringUtils.breakApart(command);
          String exitCode = ProcessManager.getProcessByID("echo").stdout(com);
          Debug.processCode(exitCode);
        }

        if(command.startsWith("exit")) {
          String exitCode = ProcessManager.getProcessByID("exit").stdout(null);
          Debug.processCode(exitCode);
        }

        if(command.startsWith("clear")) {
          String exitCode = ProcessManager.getProcessByID("clear").stdout(null);
          Debug.processCode(exitCode);
        }

        if(command.startsWith("test")) {
          String exitCode = ProcessManager.getProcessByID("test").stdout(null);
          Debug.processCode(exitCode);
        }
      }
    });
  }
}