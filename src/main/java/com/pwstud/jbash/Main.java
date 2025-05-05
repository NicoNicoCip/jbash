package com.pwstud.jbash;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.Input;
import com.pwstud.jbash.shell.process.ProcessManager;
import com.pwstud.jbash.utils.LogsManager;

public class Main {

  public static void main(String[] args) {
    Input.create();
    LogsManager.initLogRedirection();
    Debug.log("Started Engine!", "\n");
    try {
      while (true) {
        String exitCode = ProcessManager.getProcessByID("system").stdout(null);
        if (exitCode.equals("0"))
          return;
      }
    } catch (Exception e) {
      Debug.logError(e);
    }
  }
}