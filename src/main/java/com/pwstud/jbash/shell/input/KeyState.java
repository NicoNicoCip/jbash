package com.pwstud.jbash.shell.input;

public enum KeyState {
  NONE(-1),
  PRESSED(0),
  HELD(1),
  RELEASED(2);

  byte stateNum = -1;

  private KeyState(int stateNumber) {
    this.stateNum = (byte)stateNumber;
  }

  public void stepState() {
    this.stateNum++;
    if(this.stateNum > 2)
      this.stateNum = -1;
  }

  public byte toNum() {
    return this.stateNum;
  }

  @Override
  public String toString() {
    String state = "";
    switch (this.stateNum) {
      case 0 -> {
        state = "PRESSED";
      }

      case 1 -> {
        state = "HELD";
      }

      case 2 -> {
        state = "RELEASED";
      }

      default -> {
        state = "NONE";
      }
    }
    return "\"state\"=\"" + state + "\"";
  }

  public static KeyState fromString(String value) {
    KeyState state = NONE;
    switch (value) {
      case "PRESSED" -> {
        state = PRESSED;
      }

      case "HELD" -> {
        state = HELD;
      }

      case "RELEASED" -> {
        state = RELEASED;
      }

      default -> {
        state = NONE;
      }
    }
    return state;
  }

  public static KeyState fromNum(int value) {
    KeyState state = NONE;
    switch (value) {
      case 0 -> {
        state = PRESSED;
      }

      case 1 -> {
        state = HELD;
      }

      case 2 -> {
        state = RELEASED;
      }

      default -> {
        state = NONE;
      }
    }
    return state;
  }
}
