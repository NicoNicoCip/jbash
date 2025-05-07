package com.pwstud.jbash.scripts;

import com.pwstud.jbash.core.Script;
import com.pwstud.jbash.shell.process.ProcessManager;
import com.pwstud.jbash.shell.rendering.Color;
import com.pwstud.jbash.shell.rendering.Drawer;

public class RapiTester extends Script {
  Drawer drawer = new Drawer();

  @Override
  public void Instantiate() {
    setLayer(3);
  }

  @Override
  public void Start() {
    ProcessManager.getProcessByID("clear").stdout(null);
    String chars = """
    +-----------+
    |   HELLO   |
    |   WORLD   |
    +-----------+                           .""";

    drawer.stringToPixelChars(chars);
    drawer.setBackgroundColor(new Color(0, 0, 255));
    drawer.setColor(new Color(255, 255, 255));
    Drawer.disableCursor();
    Drawer.enableMouseSupport();
  }

  @Override
  public void Update() {
    drawer.out();
  }

  @Override
  public void Close() {
    ProcessManager.getProcessByID("clear").stdout(null);
    Drawer.enableCursor();
    Drawer.disableMouseSupport();
  }
}
