package com.pwstud.jbash.shell.processes;

import com.pwstud.jbash.Main;
import com.pwstud.jbash.debug.Debug;

public class system extends JBASHProcess {
  @Override
  public void create() {
    Debug.logn("System Loaded");
    Debug.logn(this.getSerial());
  }

  @Override
  public void update() {
    while (running) {
      JBASHProcess proc = Main.jbashpm.getProcessByID("bash:0");
      proc.stdIn(null);
      proc.create();
      proc.update();
      stdOut("COMPLETED");
    }
  }
}
