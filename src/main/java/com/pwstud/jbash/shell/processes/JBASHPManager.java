package com.pwstud.jbash.shell.processes;

import java.util.List;

import com.pwstud.jbash.utils.ClassHook;

public class JBASHPManager{
  private List<JBASHProcess> processes = ClassHook.getInstancesOfType(JBASHProcess.class, "com.pwstud.jbash.shell");

  public JBASHProcess getProcessByID(String id) {
    for (JBASHProcess jproc : processes) {
      if(jproc.getClass().getSimpleName().equals(id)){
        return jproc;
      }
    }

    return null;
  }
}
