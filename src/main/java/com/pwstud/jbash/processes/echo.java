package com.pwstud.jbash.processes;

import java.util.Arrays;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.process.JBashProcess;

public class echo extends JBashProcess {

  @Override
  public String stdout(String[] stdin) {
    stdin = Arrays.copyOfRange(stdin, 1, stdin.length);

    for (int i=0; i < stdin.length; i++) {
      Debug.logn(stdin[i]);
    }
    return "1";
  }
}
