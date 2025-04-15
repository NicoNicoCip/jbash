package com.pwstud.jbash;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.Input;
import com.pwstud.jbash.shell.process.JBASHPManager;
import com.pwstud.jbash.utils.LogsManager;

public class Main {
  public static final JBASHPManager jbashpm = new JBASHPManager();

  //TODO: The logs dont have a >>, and no command that was ran. FIX IT.

  public static void main(String[] args) {
    Input.create();
    LogsManager.initLogRedirection();
    Debug.logn("Started Engine!");
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