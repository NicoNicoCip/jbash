package com.pwstud.jbash.processes;

import com.pwstud.jbash.shell.process.JBashProcess;

public class exit extends JBashProcess{

  @Override
  public String stdout(String[] stdin) {
    return "0";
  }
  
}
