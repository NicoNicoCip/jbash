package com.pwstud.jbash.shell.processes;

public class exit implements JBASHProcess{

  @Override
  public String stdout(String[] stdin) {
    return "0";
  }
  
}
