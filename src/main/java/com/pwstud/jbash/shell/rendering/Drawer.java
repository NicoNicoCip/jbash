package com.pwstud.jbash.shell.rendering;


import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.shell.input.Input;

public class Drawer {
  // this is used for creating and holding the pixel data used in the displaying
  // of anything.

  // the way it works is simple. you can add multiple layers to the pixel grid,
  // and you can add it to a specific layer. In general, a layer with a greater
  // index will be drawn over a layer with a lower one. (0 draws under 1, 1 draws
  // under 2, etc.) All the layers then get crunched to the pixelGrid matrix that
  // gets drawn to the screen.
  private int height = 32;
  private int width = 32;
  private Pixel[][] pixelGrid = new Pixel[height][width];

  public static void disableCursor() {
    Debug.out("\u001B[?25l");
  }

  public static void enableCursor() {
    Debug.out("\u001B[?25h");
  }

  public static void homeCursor() {
    Debug.out("\u001B[H");
  }

  public static void enableMouseSupport() {
    Debug.out("\u001B[?1003h");
  }

  public static void disableMouseSupport() {
    Debug.out("\u001B[?1003l");
  }

  public void out() {
    StringBuffer buffer = new StringBuffer();
    homeCursor();

    for (int y = 0; y < pixelGrid.length; y++) {
      for (int x = 0; x < pixelGrid[y].length; x++) {
        buffer
        .append(pixelGrid[y][x].backgroundColor.formatBackground())
        .append(pixelGrid[y][x].color.formatColor());

        if (pixelGrid[y][x].character == 0)
          buffer.append(" ");
        else
          buffer.append(pixelGrid[y][x].character);
      }
      buffer.append(Color.ACCC);
      if (y != height - 1)
        buffer.append("\n");
    }
    Debug.out(buffer.toString());
  }

  public void stringToPixelChars(String pixelChars) {
    String[] lines = pixelChars.split("\n");
    this.pixelGrid = new Pixel[lines.length][0];
    for (int y = 0; y < lines.length; y++) {
      char[] charLine = lines[y].toCharArray();
      this.pixelGrid[y] = new Pixel[charLine.length];
      for (int x = 0; x < charLine.length; x++) {
        this.pixelGrid[y][x] = new Pixel();
      }
    }

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

  public void updateBounds() {
    int[] newBounds = Input.getBounds();
    if (width != newBounds[1] || height != newBounds[0]) {
      width = newBounds[0];
      height = newBounds[1];
      Pixel[][] newGrid = new Pixel[height][width];
      Pixel lasnNotNull = new Pixel();
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (y < pixelGrid.length && x < pixelGrid[0].length) {
            newGrid[y][x] = pixelGrid[y][x]; // copy existing pixel
            lasnNotNull = pixelGrid[y][x];
          } else {
            newGrid[y][x] = lasnNotNull; // create new pixel if outside old bounds
          }
        }
      }

      pixelGrid = newGrid;
    }
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
