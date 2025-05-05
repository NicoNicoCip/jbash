package com.pwstud.jbash.core;

public abstract class Script {
  private byte layer = 0;
  private boolean running = true;
  /**
   * Optional method used in the initialization of scripts. This will allways eb ran before 
   * the start method, regardless of if the runner is layered or not.
   */
  public void Instantiate() {}

  /**
   * Abstract function that runs once before all the update functions. Note that in the updating of scripts,
   * first all the start functions will be ran, in the layer, after the Instantiate functions are ran. If 
   * using the {@link ScriptManager#runAll()}, the same will be done, but ignoring the layer of the script.
   */
  public abstract void Start();

  /**
   * Abstarct function that runs each frame of the engine in the current layer. Note tha tin the updating of scriptrs,
   * this function is ran after the start method. If using {@link ScriptManager#runAll()} , the same will be done 
   * but ignoring the layers.
   */
  public abstract void Update();

  /**
   * Optional method for doing something when the scipt ends its execution (When the private boolean of the Script
   * "running" is false)
   */
  public void End() {}

  /**
   * Optional method for doing somehting when the entire script manager stops. (When the private boolean od the ScriptManager
   * "running" is false)
   */
  public void Close() {}

  public byte getLayer() {
    return layer;
  }

  public boolean getRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public void setLayer(int layer) {
    setLayer((byte) layer);
  }

  public void setLayer(byte layer) {
    this.layer = layer;
  }
}
