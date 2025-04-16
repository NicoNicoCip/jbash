package com.pwstud.jbash.processes;

import java.util.Arrays;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.process.JBashProcess;

public class echo extends JBashProcess {

  @Override
  public String stdout(String[] stdin) {
    stdin = Arrays.copyOfRange(stdin, 1, stdin.length);
    boolean printed = false;
    for (String in : stdin) {
      Debug.bulkIn(in, " ");
      printed = true;
    }
    Debug.bulkOut();
    if(printed) Debug.logn();
    return "1";
  }
}
