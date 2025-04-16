package com.pwstud.jbash.processes;

import com.pwstud.jbash.shell.process.JBashProcess;

public class test extends JBashProcess{

  @Override
  public String stdout(String[] stdin) {
    new rapi().stdrun();
    return "1";
  }
  
}
