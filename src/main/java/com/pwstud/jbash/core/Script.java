package com.pwstud.jbash.core;

public abstract class Script {
  private byte layer = 0;
  public abstract void Start();
  public abstract void Update();

  public byte getLayer() {
    return layer;
  }

  public void setLayer(byte layer) {
    this.layer = layer;
  }
}
