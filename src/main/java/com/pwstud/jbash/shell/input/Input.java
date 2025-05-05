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
import org.jline.terminal.TerminalBuilder;

import com.pwstud.jbash.debug.Debug;
import com.pwstud.jbash.utils.LogsManager;

public class Input {
  public static LineReader reader;
  private static Path historyFile = Paths.get("/src/main/resources/system/.historial");
  private static int mouseX = 0;
  private static int mouseY = 0;
  private static int mouseButton = 0;

  public static void create() {
    try {
      reader = LineReaderBuilder.builder()
          .terminal(TerminalBuilder.builder()
          .system(false)
          .build())
          .completer(new Completer() {

            @Override
            public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
              candidates.add(new Candidate("echo"));
              candidates.add(new Candidate("exit"));
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

      return (char)ch; // Normal character
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