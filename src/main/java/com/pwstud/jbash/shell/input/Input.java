package com.pwstud.jbash.shell.input;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.TerminalBuilder;

import com.pwstud.jbash.debug.Debug;

public class Input {
  private static Logger jLineLogger = Logger.getLogger("org.jline");
  private static Logger rootLogger = Logger.getLogger("");
  private static Path historyFile = Paths.get("data/system/.historial");

  // enable logging by setting this to Level.FINEST or Level.OFF to disable
  private static final Level state = Level.OFF;

  public static LineReader reader;

  public static void create() {
    rootLogger.setLevel(state);
    jLineLogger.setLevel(state);
    rootLogger.getHandlers()[0].setLevel(state);

    try {
      reader = LineReaderBuilder.builder()
          .terminal(TerminalBuilder.builder()
              .system(true)
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

  public static String read() {
    return reader.readLine(">> ").replaceAll("`\n", "\n");
  }

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
