package com.pwstud.jbash.shell.processes;

import java.util.Arrays;

import com.pwstud.jbash.Main;
import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.Input;
import com.pwstud.jbash.utils.StringUtils;

public class bash extends JBASHProcess {
  Input input = new Input();

  @Override
  public void create() {
    Debug.logn("Entered bash");
    try {
      input.TerminalBASIC();
    } catch (Exception e) {
      Debug.logError(e);
    }
  }

  @Override
  public void update() {
    while (running) {
      String in = input.read();
      String[] command = StringUtils.breakApart(in);
      JBASHProcess proc = Main.jbashpm.getProcessByID(command[0].toLowerCase() + ":0");
      proc.stdIn(command);
      stdOut(proc.stdOut());
    }
  }
}
