package com.pwstud.jbash.shell.processes;

import com.pwstud.jbash.debug.Debug;

public class echo extends JBASHProcess{
  @Override
  public void create() {
    String[] stdIn = stdIn();
    for (String in : stdIn) {
      Debug.bulkIn(in, " ");
    }
    Debug.bulkOut();
    Debug.logn("I RAN!!!");
  }

  @Override
  public void update() {
    // unimplemented
  }
}
