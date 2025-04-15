package com.pwstud.jbash.processes;

import com.pwstud.jbash.core.ScriptManager;
import com.pwstud.jbash.shell.process.JBASHProcess;

public class rusc implements JBASHProcess{

  @Override
  public String stdout(String[] stdin) {
    ScriptManager.runLayered();
    return "1";
  }
  
}
