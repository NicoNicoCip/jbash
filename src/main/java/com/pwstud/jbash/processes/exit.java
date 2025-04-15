package com.pwstud.jbash.processes;

import com.pwstud.jbash.shell.process.JBASHProcess;

public class exit implements JBASHProcess{

  @Override
  public String stdout(String[] stdin) {
    return "0";
  }
  
}
