package com.pwstud.jbash.processes;

import com.pwstud.jbash.Main;
import com.pwstud.jbash.shell.process.JBASHProcess;

public class system implements JBASHProcess {

  /*
   * This process manages and keeps a index of all the processes ran and their id.
   * THis can be used to switch between processes based on the ones that were allready ran.
   * 
   * VERSION 1.0:
   *  Runs the base process by default and nothing more.
   */
  public static JBASHProcess lastProcess = null;

  @Override
  public String stdout(String[] stdin) {
    lastProcess = Main.jbashpm.getProcessByID("bash");
    return lastProcess.stdout(null);
  }
}
