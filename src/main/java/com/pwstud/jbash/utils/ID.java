package com.pwstud.jbash.utils;

import java.util.HashMap;

public abstract class ID {
  private final StringBuffer serial = new StringBuffer();
  private static HashMap<String, Integer> serialIndex = new HashMap<>();

  public void initID() {
    String classString = this.getClass().getSimpleName();
    if (!serialIndex.containsKey(classString))
      serialIndex.put(classString, 0);

    int sip = serialIndex.get(classString);

    this.serial.append(classString).append(":").append(sip);

    sip++;
    serialIndex.put(classString, sip);
  }

  public String getSerial() {
    return serial.toString();
  }
}
