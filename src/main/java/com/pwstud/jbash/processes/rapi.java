package com.pwstud.jbash.processes;

import com.pwstud.jbash.Main;
import com.pwstud.jbash.shell.process.RapiProcess;
import com.pwstud.jbash.shell.rendering.Color;
import com.pwstud.jbash.shell.rendering.Drawer;

public class rapi extends RapiProcess {

  // the process starts by taking in several settings:
  // 1. the rendering process. for example for ui you would have rui_mainMenu
  // 2. the settings for the rendering. framerate, input processing (like mouse or
  // keyboard)
  // 3. it will start rendering it.

  @Override
  public void stdrun() {
    Main.jbashpm.getProcessByID("clear").stdout(null);
    String chars = """
    +-----------+
    |   HELLO   |
    |   WORLD   |
    +-----------+""";
    Drawer drawer = new Drawer(13, 4);
    drawer.stringToPixelChars(chars);
    drawer.setBackgroundColor(new Color(0, 0, 255));
    drawer.setColor(new Color(255, 255, 255));
    while (true) {
      drawer.out();
    }
  }
}
