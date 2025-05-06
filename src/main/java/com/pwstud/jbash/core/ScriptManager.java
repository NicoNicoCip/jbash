package com.pwstud.jbash.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.utils.ClassHook;

/**
 * Manager Class that instanciates all the scripts inaide the
 * "com.pwstud.jbash.scripts" package
 * and packs them up neatly based on their layers. Note that currently the
 * layering system
 * works on a first come first serve system. That meas that you can change the
 * current layer
 * of scripts you want to run on the frame, but, future start and update methods
 * that
 * shouldve been run instead get skipped entirely. Keep this in mind while
 * developing
 * 
 * @author Cirnicianu Ciprian Nicolae
 * 
 * @version 1.0
 */
public class ScriptManager {
  private static List<Script> scripts = ClassHook.getInstancesOfType(Script.class, "com.pwstud.jbash.scripts");
  private static byte currentLayer = 0;
  private static boolean switchedLayer = false;
  private static boolean running = true;

  /**
   * runs all the loaded scripts, and packs the scripts on their layers.
   * Note that that the layers reset each time this function is ran, and the
   * current layer gets set to 0.
   */
  public static void runLayered() {
    HashMap<Byte, List<Script>> layeredScripts = new HashMap<>();

    for (Script script : scripts) {
      script.Instantiate();
      if (!layeredScripts.containsKey(script.getLayer()))
        layeredScripts.put(script.getLayer(), new ArrayList<>());

      layeredScripts.get(script.getLayer()).add(script);
    }

    if(layeredScripts.size() == 0) {
      Debug.log("Something Went wrong in loading the scripts.");
      return;
    }

    while (running) {
      switchedLayer = false;
      for (Script scriptLayer : layeredScripts.get(currentLayer)) {
        if(layeredScripts.size() == 0) {
          Debug.log("The scripts on layer " + currentLayer + " not found.");
          return;
        }

        scriptLayer.Start();
        if (switchedLayer)
          break;
      }

      while (running) {
        for (Script scriptLayer : layeredScripts.get(currentLayer)) {
          scriptLayer.Update();

          if (!scriptLayer.getRunning()) {
            break;
          }

          // if (Input.readFreed() == CharList.CTRL_X) {
          //   setRunning(false);
          //   break;
          // }

          // while (Input.readFreed() == CharList.CTRL_S) {
          //   Debug.out("PAUSED . . .");
          // }

          if (switchedLayer || !scriptLayer.getRunning())
            break;
        }
      }

      for (Script scriptLayer : layeredScripts.get(currentLayer)) {
        scriptLayer.End();
      }
    }

    for (Script scriptLayer : layeredScripts.get(currentLayer)) {
      scriptLayer.Close();
    }

    running = true;
  }

  /**
   * runs all the loaded scripts, regardless of their layers. It is highly
   * recomended that
   * you dont use this, unless it is for verry specific reasons, like lag,
   * performance,
   * optimisations, stability. However, this is way les robust than the layered
   * version.
   */
  public static void runAll() {
    while (running) {
      for (Script scriptLayer : scripts) {
        scriptLayer.Instantiate();
      }

      for (Script scriptLayer : scripts) {
        scriptLayer.Start();
      }

      while (running) {
        for (Script scriptLayer : scripts) {
          scriptLayer.Update();
        }
      }
    }

    running = true;
  }

  public static byte getCurrentLayer() {
    return currentLayer;
  }

  public static boolean getRunning() {
    return running;
  }

  public static void setCurrentLayer(int currentLayer) {
    setCurrentLayer((byte) currentLayer);
  }

  public static void setCurrentLayer(byte currentLayer) {
    ScriptManager.currentLayer = currentLayer;
    switchedLayer = true;
  }

  public static void setRunning(boolean running) {
    ScriptManager.running = running;
  }
}
