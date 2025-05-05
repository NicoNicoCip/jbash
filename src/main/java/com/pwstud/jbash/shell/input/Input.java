package com.pwstud.jbash.shell.input;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.MouseEvent;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Display;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.utils.LogsManager;

public class Input {
  public static LineReader reader;
  public static Terminal jlineTerminal;
  public static Display display;
  private static Path historyFile = Paths.get("/src/main/resources/system/.historial");
  private static int mouseX = 0;
  private static int mouseY = 0;
  private static int mouseButton = 0;
  private static int termRows;
  private static int termColums;

  public static void create() {
    try {
      jlineTerminal = TerminalBuilder.builder()
          .system(true)
          .build();

      reader = LineReaderBuilder.builder()
          .terminal(jlineTerminal)
          .completer(new Completer() {

            @Override
            public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
              candidates.add(new Candidate("echo"));
              candidates.add(new Candidate("exit"));
              candidates.add(new Candidate("test"));
            }
          })
          .variable(LineReader.SECONDARY_PROMPT_PATTERN, "> ")
          .history(new DefaultHistory())
          .variable(LineReader.HISTORY_FILE, historyFile)
          .variable(LineReader.HISTORY_SIZE, 1000)
          .build();
    } catch (IOException e) {
      Debug.logError(e);
    }
  }

  public static String readBlocking() {
    String out = reader.readLine(">> ").replaceAll("`\n", "\n");
    LogsManager.out(">> " + out);
    return out;
  }

  public static char readFreed() {
    try {
      int ch = reader.getTerminal().reader().read();
      if (ch < 0) {
        return CharList.none;
      }

      if (ch == CharList.ESC) { // Escape sequence
        reader.getTerminal().writer().flush();
        ch = reader.getTerminal().reader().read();
        if (ch == '[' || ch == 79) {
          ch = reader.getTerminal().reader().read();
          switch (ch) {
            case 65:
              return CharList.UP_ARROW;
            case 66:
              return CharList.DOWN_ARROW;
            case 67:
              return CharList.RIGHT_ARROW;
            case 68:
              return CharList.LEFT_ARROW;
          }

          if (ch == 'M') { // Mouse event
            MouseEvent mEvent = reader.getTerminal().readMouseEvent();
            mouseX = mEvent.getX();
            mouseY = mEvent.getY();
            mouseButton = mEvent.getButton().ordinal();
          }
        }
        return CharList.ESC; // Just ESC pressed
      }

      return (char) ch; // Normal character
    } catch (IOException e) {
      Debug.logError(e);
    }
    return CharList.none; // No input
  }

  public static int[] getMouse() {
    return new int[] {
        mouseButton,
        mouseX,
        mouseY
    };
  }

  public static int[] getBounds() {
    return new int[] {
        termColums,
        termRows
    };
  }

  public static void resizeDaemon() {
    updateBounds(); // initialize

    Thread resizeWatcher = new Thread(() -> {
      int lastCols = termColums;
      int lastRows = termRows;

      while (true) {
        Size currentSize = jlineTerminal.getSize();
        int newCols = currentSize.getColumns();
        int newRows = currentSize.getRows();

        if (newCols != lastCols || newRows != lastRows) {
          updateBounds();
          lastCols = newCols;
          lastRows = newRows;
        }

        try {
          Thread.sleep(200); // check 5x/sec
        } catch (InterruptedException e) {
          break;
        }
      }
    });

    resizeWatcher.setDaemon(true);
    resizeWatcher.start();
  }

  public static void updateBounds() {
    Size s = jlineTerminal.getSize();
    termRows = s.getRows();
    termColums = s.getColumns();
  
    // Resize internal display state
    if (display == null) {
      display = new Display(jlineTerminal, false);
    }
    display.resize(s.getRows(), s.getColumns());
  }
  
  // TODO: Move this function to a better place.
  public static <T> T[] removeObject(T[] array, T other) {
    T[] newArray = null;
    int index = 0;

    for (T element : array) {
      if (!element.equals(other)) {
        if (index == 0)
          newArray = Arrays.copyOf(array, 0);

        newArray = Arrays.copyOf(newArray, index + 1);
        newArray[index] = element;
      }
    }

    return newArray;
  }
}