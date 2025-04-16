package com.pwstud.jbash;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.Input;
import com.pwstud.jbash.shell.process.ProcessManager;
import com.pwstud.jbash.utils.LogsManager;

public class Main {
  public static final ProcessManager jbashpm = new ProcessManager();

  public static void main(String[] args) {
    Input.create();
    LogsManager.initLogRedirection();
    Debug.log("Started Engine!", "\n");
    try {
      while (true) {
        String exitCode = jbashpm.getProcessByID("system").stdout(null);
        if(exitCode.equals("0")) return;
      }
    } catch (Exception e) {
      Debug.logError(e);
    }
  }
}