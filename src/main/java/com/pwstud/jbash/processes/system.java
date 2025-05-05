package com.pwstud.jbash.processes;

import com.pwstud.jbash.shell.process.JBashProcess;
import com.pwstud.jbash.shell.process.ProcessManager;

public class system extends JBashProcess {

  /*
   * This process manages and keeps a index of all the processes ran and their id.
   * THis can be used to switch between processes based on the ones that were
   * allready ran.
   * 
   * VERSION 1.0:
   * Runs the base process by default and nothing more.
   */
  public static JBashProcess lastProcess = null;

  @Override
  public String stdout(String[] stdin) {
    lastProcess = ProcessManager.getProcessByID("bash");
    return lastProcess.stdout(null);
  }
}
