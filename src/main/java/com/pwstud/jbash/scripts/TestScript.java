package com.pwstud.jbash.scripts;

import com.pwstud.jbash.core.Script;
import com.pwstud.jbash.debug.Debug;

public class TestScript extends Script {

  @Override
  public void Start() {
    Debug.log("Lets start \n");
  }

  int crono = 0;
  @Override
  public void Update() {
    Debug.log(crono + "\t");
    crono ++;
  }
}
