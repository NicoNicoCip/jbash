package com.pwstud.jbash.shell.input;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

public class Input {
  Logger jLineLogger = Logger.getLogger("org.jline");
  Logger rootLogger = Logger.getLogger("");
  Path historyFile = Paths.get(System.getProperty("user.home"), "/Jbash/.jbashHistory");

  // enable logging by setting this to Level.FINEST
  Level state = Level.OFF;

  LineReader reader;

  public void TerminalBASIC() throws IOException {
    rootLogger.setLevel(state);
    jLineLogger.setLevel(state);
    rootLogger.getHandlers()[0].setLevel(state);

    reader = LineReaderBuilder.builder()
        .terminal(TerminalBuilder.builder()
            .system(true)
            .build())
        .completer(new Completer() {
          @Override
          public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            String partial = line.word();
            if (partial.startsWith("m")) {
              candidates.add(new Candidate("mega"));
              candidates.add(new Candidate("mockup"));
              candidates.add(new Candidate("mold"));
            }
            // Add other completion rules...
          }
        })
        .variable(LineReader.SECONDARY_PROMPT_PATTERN, "> ")
        .history(new DefaultHistory())
        .variable(LineReader.HISTORY_FILE, historyFile)
        .variable(LineReader.HISTORY_SIZE, 1000)
        .build();
  }

  public String read() {
    return reader.readLine(">> ").replaceAll("`\n", "\n");
  }
}
