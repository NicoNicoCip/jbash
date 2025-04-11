package com.pwstud.jbash;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.debug.LogsManager;
import com.pwstud.jbash.shell.processes.JBASHPManager;
import com.pwstud.jbash.shell.processes.JBASHProcess;

public class Main {
  public static final JBASHPManager jbashpm = new JBASHPManager();

  public static void main(String[] args) {
    LogsManager.createLogFile();
    try {

      JBASHProcess proc = jbashpm.getProcessByID("system:0");
      proc.create();
      proc.update();
    } catch (Exception e) {
      Debug.logError(e);
    }
  }
}