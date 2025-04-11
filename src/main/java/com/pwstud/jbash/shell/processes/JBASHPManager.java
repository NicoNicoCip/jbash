package com.pwstud.jbash.shell.processes;

import java.util.ArrayList;
import java.util.List;

import com.pwstud.jbash.utils.ClassHook;

public class JBASHPManager extends ClassHook<JBASHProcess>{
  private List<JBASHProcess> processes = new ArrayList<>();

  public JBASHPManager() {
    this.load(JBASHProcess.class, "extends JBASHProcess");
  }

  @Override
  public void runOn(JBASHProcess instance) {
    instance.initID();
    processes.add(instance);
  }

  public JBASHProcess getProcessByID(String id) {
    for (JBASHProcess jproc : processes) {
      if(jproc.getSerial().equals(id)) {
        return jproc;
      }
    }

    return null;
  }
}
