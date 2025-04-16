package com.pwstud.jbash.shell.rendering;

public class Color {
  public static String ACCC = "\u001B[0m";

  public int r = 0;
  public int g = 0;
  public int b = 0;

  public Color() {
  }

  public Color(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public String toString() {
    return this.formatColor();
  }

  public String toBackgroundString() {
    return this.formatBackground();
  }

  /**
   * @return A string representing the ANSI character for the inputted rgb color,
   *         for the foreground specifically.
   */
  public String formatColor() {
    return "\u001B[38;2;" + this.r + ";" + this.g + ";" + this.b + "m";
  }

  /**
   * @return A string representing the ANSI character for the inputted rgb color,
   *         for the background specifically.
   */
  public String formatBackground() {
    return "\u001B[48;2;" + this.r + ";" + this.g + ";" + this.b + "m";
  }
}
