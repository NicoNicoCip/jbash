package com.pwstud.jbash.shell.rendering;

import com.pwstud.jbash.debug.Debug;

public class Drawer {
  // this is used for creating and holding the pixel data used in the displaying
  // of anything.
  private int height = 4;
  private int width = 16;
  private Pixel[][] pixelGrid = new Pixel[height][width];

  public Drawer() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.pixelGrid[y][x] = new Pixel();
      }
    }
  }

  public Drawer(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixelGrid = new Pixel[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.pixelGrid[y][x] = new Pixel();
      }
    }
  }

  public static void disableCursor() {
    Debug.out("\u001B[?25l");
  }

  public static void enableCursor() {
    Debug.out("\u001B[?25"); //TODO: fix mouse cursor enabling.
  }

  // TODO: enable the cursor after rapi end and disable it while printing.

  public void out() {
    Debug.out("\u001B[0;0H");
    StringBuffer buffer = new StringBuffer();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        buffer
            .append(pixelGrid[y][x].backgroundColor.formatBackground())
            .append(pixelGrid[y][x].color.formatColor())
            .append(pixelGrid[y][x].character);
      }
      buffer.append(Color.ACCC).append("\n");
    }
    Debug.out(buffer.toString());
  }

  public void stringToPixelChars(String pixelChars) {
    String[] lines = pixelChars.split("\n");
    for (int y = 0; y < lines.length; y++) {
      char[] charLine = lines[y].toCharArray();
      for (int x = 0; x < charLine.length; x++) {
        pixelGrid[y][x].character = charLine[x];
      }
    }
  }

  public void setBackgroundColor(Color color) {
    Pixel[][] temp = pixelGrid;
    for (Pixel[] pixels : temp) {
      for (Pixel pixel : pixels) {
        pixel.backgroundColor = color;
      }
    }
    pixelGrid = temp;
  }

  public void setColor(Color color) {
    Pixel[][] temp = pixelGrid;
    for (Pixel[] pixels : temp) {
      for (Pixel pixel : pixels) {
        pixel.color = color;
      }
    }
    pixelGrid = temp;
  }

  public Pixel[][] getPixelGrid() {
    return pixelGrid;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public void setPixelGrid(Pixel[][] pixelGrid) {
    this.pixelGrid = pixelGrid;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setWidth(int width) {
    this.width = width;
  }
}
