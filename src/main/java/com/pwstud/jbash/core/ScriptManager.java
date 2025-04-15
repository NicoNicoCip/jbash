package com.pwstud.jbash.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.pwstud.jbash.utils.ClassHook;

public class ScriptManager {
  private static List<Script> scripts = ClassHook.getInstancesOfType(Script.class, "com.pwstud.jbash.scripts");
  private static HashMap<Byte,List<Script>> layeredScripts = new HashMap<>();
  private static byte currentLayer = 0;

  // when the layer changes the loop ends and starts walking the new layer. 
  // for each layer, first all the create functions are ran in order of 
  // instantiation, which is the same as the file order then the same is done
  // for the updates, but each frame.

  public static void runAll() {
    for (Script script : scripts) {
      layeredScripts.get(script.getLayer()).add(script);
    }


    for (Script scriptLayer : layeredScripts.get(currentLayer)) {

    }
  }

  public static byte getCurrentLayer() {
    return currentLayer;
  }

  public static void setCurrentLayer(byte currentLayer) {
    ScriptManager.currentLayer = currentLayer;
  }
}
