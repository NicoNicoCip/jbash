package com.pwstud.jbash.shell.processes;

import com.pwstud.jbash.utils.ID;

public abstract class JBASHProcess extends ID {
  protected boolean running = true;
  private String[] stdIn = new String[0];
  private String stdOut = "";
  public abstract void create();
  public abstract void update();

  protected String[] stdIn() {
    return stdIn;
  }

  public String stdOut() {
    return stdOut;
  }

  public void stdIn(String[] stdIn) {
    this.stdIn = stdIn;
  }

  protected void stdOut(String stdOut) {
    this.stdOut = stdOut;
  }
}
