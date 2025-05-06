package com.pwstud.jbash.shell.input;

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.*;

public class EventBus {
  private final List<EventListener> listeners = new ArrayList<>();
  private final BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<>();

  public void register(EventListener listener) {
    listeners.add(listener);
  }

  public void unregister(EventListener listener) {
    listeners.remove(listener);
  }

  // Blocking read (poll for next event)
  public Object nextEventBlocking() throws InterruptedException {
    return blockingQueue.take();
  }

  // Non-blocking peek
  public Object pollEvent() {
    return blockingQueue.poll();
  }

  // Dispatchers
  public void fireResize(int cols, int rows) {
    for (var l : listeners)
      l.onResize(cols, rows);
    blockingQueue.offer(new ResizeEvent(cols, rows));
  }

  public void fireMouseMove(int col, int row) {
    for (var l : listeners)
      l.onMouseMove(col, row);
    blockingQueue.offer(new MouseMoveEvent(col, row));
  }

  public void fireKey(char c) {
    for (var l : listeners)
      l.onChar(c);
    blockingQueue.offer(c);
  }

  public void fireKeyPress(KeyEvent e) {
    for (var l : listeners)
      l.onKey(e);
    blockingQueue.offer(e);
  }

  public void fireCommand(String command) {
    for (var l : listeners)
      l.onCommand(command);
    blockingQueue.offer(command);
  }

  public void fireMouseClick(int col, int row, int button) {
    for (var l : listeners)
      l.onMouseClick(col, row, button);
    blockingQueue.offer(new MouseClickEvent(col, row, button));
  }

  public void fireMousePress(int col, int row, int button) {
    for (var l : listeners)
      l.onMousePress(col, row, button);
    blockingQueue.offer(new MousePressEvent(col, row, button));
  }

  public void fireMouseRelease(int col, int row, int button) {
    for (var l : listeners)
      l.onMouseRelease(col, row, button);
    blockingQueue.offer(new MouseReleaseEvent(col, row, button));
  }

  public void fireAutocomplete(String completer) {
    for (var l : listeners)
      l.onAutocomplete(completer);
    blockingQueue.offer(new AutocompleteEvent(completer));
  }

  // Event wrappers
  public record ResizeEvent(int cols, int rows) {
  }

  public record MouseMoveEvent(int col, int row) {
  }

  public record MouseClickEvent(int col, int row, int button) {
  }

  public record MousePressEvent(int col, int row, int button) {
  }

  public record MouseReleaseEvent(int col, int row, int button) {
  }

  public record AutocompleteEvent(String toComplete) {
  }
}
