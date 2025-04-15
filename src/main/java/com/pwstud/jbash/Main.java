package com.pwstud.jbash;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.debug.LogsManager;
import com.pwstud.jbash.shell.input.Input;
import com.pwstud.jbash.shell.process.JBASHPManager;

public class Main {
  public static final JBASHPManager jbashpm = new JBASHPManager();

  public static void main(String[] args) {
    Input.create();
    LogsManager.createLogFile();
    Debug.log("Started Engine! \n");
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