package com.pwstud.jbash.processes;

import com.pwstud.jbash.core.ScriptManager;
import com.pwstud.jbash.shell.process.RapiProcess;

public class rapi extends RapiProcess {

  // the process starts by taking in several settings:
  // 1. the rendering process. for example for ui you would have rui_mainMenu
  // 2. the settings for the rendering. framerate, input processing (like mouse or
  // keyboard)
  // 3. it will start rendering it.

  @Override
  public void stdrun() {
    ScriptManager.setCurrentLayer(3);
    ScriptManager.runLayered();
  }
}
