package com.pwstud.jbash.shell.process;

import java.util.List;

import com.pwstud.jbash.utils.ClassHook;

public class ProcessManager{
  private List<JBashProcess> processes = ClassHook.getInstancesOfType(JBashProcess.class, "com.pwstud.jbash.processes");

  public JBashProcess getProcessByID(String id) {
    for (JBashProcess jproc : processes) {
      if(jproc.getClass().getSimpleName().equals(id)){
        return jproc;
      }
    }

    return null;
  }
}
